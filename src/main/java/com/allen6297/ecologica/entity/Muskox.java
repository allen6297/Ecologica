package com.allen6297.ecologica.entity;

import java.util.EnumSet;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.allen6297.ecologica.registry.EcologicaEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * A heavy grazer. It eats ordinary glow lichen, walks back to the herd, and stands
 * around a calf when a wolf comes close. It does not eat the sprouted lichen that makes soil.
 */
public class Muskox extends Animal {
    private static final EntityDataAccessor<Boolean> DATA_FED = SynchedEntityData.defineId(Muskox.class, EntityDataSerializers.BOOLEAN);

    public Muskox(EntityType<? extends Muskox> type, Level level) {
        super(type, level);
    }

    /**
     * Snowy plains that have taken the deepslate column are not grass. Spawn on that rock,
     * on snow sitting on it, and on ordinary animal ground so a pack's tundra still works.
     */
    public static boolean checkSpawnRules(
            EntityType<Muskox> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        var below = level.getBlockState(pos.below());
        boolean ground = below.is(BlockTags.ANIMALS_SPAWNABLE_ON)
                || below.is(Blocks.DEEPSLATE)
                || below.is(Blocks.SMOOTH_BASALT)
                || below.is(Blocks.SNOW);
        boolean bright = EntitySpawnReason.ignoresLightRequirements(reason) || level.getRawBrightness(pos, 0) > 8;
        return ground && bright;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.FOLLOW_RANGE, 18.0);
    }

    public boolean isFed() {
        return this.entityData.get(DATA_FED);
    }

    public void setFed(boolean fed) {
        this.entityData.set(DATA_FED, fed);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FED, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Fed", this.isFed());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setFed(input.getBooleanOr("Fed", false));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new DefendCalvesGoal(this));
        this.goalSelector.addGoal(2, new GrazeLichenGoal(this));
        this.goalSelector.addGoal(3, new FollowHerdGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable Muskox getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return EcologicaEntities.MUSKOX.get().create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.SHEARS)) {
            ItemStack drop = MuskoxHerd.qiviutDrop(this);
            if (drop.isEmpty()) {
                return InteractionResult.PASS;
            }
            if (this.level() instanceof ServerLevel server) {
                this.spawnAtLocation(server, drop);
                this.setFed(false);
            }
            stack.hurtAndBreak(1, player, hand);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        ItemStack qiviut = MuskoxHerd.qiviutDrop(this);
        if (!qiviut.isEmpty()) {
            this.spawnAtLocation(level, qiviut);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GOAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.GOAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GOAT_DEATH;
    }

    private static final class GrazeLichenGoal extends MoveToBlockGoal {
        private final Muskox muskox;

        private GrazeLichenGoal(Muskox muskox) {
            super(muskox, 1.0, 12);
            this.muskox = muskox;
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            return level.getBlockState(pos).is(Blocks.GLOW_LICHEN);
        }

        @Override
        public void tick() {
            super.tick();
            if (this.isReachedTarget()) {
                MuskoxHerd.tryGraze(this.muskox);
            }
        }
    }

    private static final class FollowHerdGoal extends Goal {
        private final Muskox muskox;
        private @Nullable Muskox mate;

        private FollowHerdGoal(Muskox muskox) {
            this.muskox = muskox;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.mate = MuskoxHerd.herdMate(this.muskox).orElse(null);
            return this.mate != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.mate != null && this.mate.isAlive() && this.muskox.distanceToSqr(this.mate) > MuskoxHerd.FOLLOW_NEAR * MuskoxHerd.FOLLOW_NEAR;
        }

        @Override
        public void tick() {
            if (this.mate != null) {
                this.muskox.getNavigation().moveTo(this.mate, 0.9);
            }
        }
    }

    private static final class DefendCalvesGoal extends Goal {
        private final Muskox muskox;
        private Vec3 slot = Vec3.ZERO;

        private DefendCalvesGoal(Muskox muskox) {
            this.muskox = muskox;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            Optional<Vec3> slot = MuskoxHerd.ringSlot(this.muskox);
            slot.ifPresent(value -> this.slot = value);
            return slot.isPresent();
        }

        @Override
        public void tick() {
            MuskoxHerd.ringSlot(this.muskox).ifPresent(value -> {
                this.slot = value;
                if (this.muskox.distanceToSqr(this.slot) > 1.5) {
                    this.muskox.getNavigation().moveTo(this.slot.x, this.slot.y, this.slot.z, 1.05);
                }
            });
        }
    }
}
