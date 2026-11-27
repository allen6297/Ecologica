package com.allen6297.ecologica.block;

import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.tag.EcologicaTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;

/**
 * Glow lichen that has been fed. In sunlight it either breaks the rock under it one step
 * toward soil, or it spreads ordinary glow lichen. Shears pause it. Mud is not food.
 */
public class SproutedGlowLichenBlock extends CarpetBlock {
    public static final int SPREAD_RANGE = 8;
    /** One in this many random ticks weathers the block underneath. */
    public static final int WEATHER_ODDS = 4;
    /** One in this many random ticks tries to spread ordinary lichen. */
    public static final int SPREAD_ODDS = 4;

    private final boolean active;

    public SproutedGlowLichenBlock(BlockBehaviour.Properties properties, boolean active) {
        super(properties);
        this.active = active;
    }

    public static void onBonemeal(BonemealEvent event) {
        if (!event.getState().is(Blocks.GLOW_LICHEN) || !canSitOn(event.getLevel(), event.getPos())) {
            return;
        }
        event.setCanceled(true);
        event.setSuccessful(true);
        if (event.getLevel() instanceof ServerLevel server) {
            server.setBlock(event.getPos(), EcologicaBlocks.SPROUTED_GLOW_LICHEN.get().defaultBlockState(), 3);
        }
    }

    public static boolean canSitOn(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(EcologicaTags.Blocks.LICHEN_SUPPORTS);
    }

    /**
     * The next block in the weathering chain, or null when this block is not eaten.
     * Mud is deliberately null: a fen's mud is not the lichen's substrate.
     */
    public static Block weathered(BlockState state) {
        if (state.is(Blocks.STONE)
                || state.is(Blocks.DEEPSLATE)
                || state.is(Blocks.SMOOTH_BASALT)
                || state.is(Blocks.COBBLESTONE)
                || state.is(Blocks.TUFF)
                || state.is(Blocks.ANDESITE)
                || state.is(Blocks.DIORITE)
                || state.is(Blocks.GRANITE)) {
            return Blocks.GRAVEL;
        }
        if (state.is(Blocks.GRAVEL)) {
            return Blocks.SAND;
        }
        if (state.is(Blocks.SAND)) {
            return Blocks.DIRT;
        }
        return null;
    }

    public static boolean seesSun(LevelReader level, BlockPos pos) {
        if (!(level.getBlockState(pos).getBlock() instanceof SproutedGlowLichenBlock lichen) || !lichen.active) {
            return false;
        }
        // canSeeSky reads a heightmap that a freshly placed block has not updated yet.
        BlockPos.MutableBlockPos above = pos.mutable();
        for (int y = pos.getY() + 1; y < level.getMaxY(); y++) {
            above.setY(y);
            if (level.getBlockState(above).canOcclude()) {
                return false;
            }
        }
        return true;
    }

    public static boolean tryWeather(LevelAccessor level, BlockPos pos) {
        if (!(level.getBlockState(pos).getBlock() instanceof SproutedGlowLichenBlock lichen) || !lichen.active) {
            return false;
        }
        BlockPos below = pos.below();
        Block next = weathered(level.getBlockState(below));
        if (next == null) {
            return false;
        }
        level.setBlock(below, next.defaultBlockState(), 3);
        return true;
    }

    public static boolean spreadToward(LevelAccessor level, BlockPos origin, BlockPos target) {
        int dx = target.getX() - origin.getX();
        int dy = target.getY() - origin.getY();
        int dz = target.getZ() - origin.getZ();
        if (dx * dx + dz * dz > SPREAD_RANGE * SPREAD_RANGE || Math.abs(dy) > 2) {
            return false;
        }
        if (!level.getBlockState(target).isAir() || !canSitOn(level, target)) {
            return false;
        }
        BlockState lichen = Blocks.GLOW_LICHEN.defaultBlockState()
                .setValue(MultifaceBlock.getFaceProperty(Direction.DOWN), true);
        if (!lichen.canSurvive(level, target)) {
            return false;
        }
        level.setBlock(target, lichen, 3);
        return true;
    }

    public static boolean trySpread(ServerLevel level, BlockPos origin, RandomSource random) {
        BlockPos target = origin.offset(random.nextInt(SPREAD_RANGE * 2 + 1) - SPREAD_RANGE, random.nextInt(5) - 2, random.nextInt(SPREAD_RANGE * 2 + 1) - SPREAD_RANGE);
        return spreadToward(level, origin, target);
    }

    public static void shear(LevelAccessor level, BlockPos pos) {
        level.setBlock(pos, EcologicaBlocks.SHEARED_GLOW_LICHEN.get().defaultBlockState(), 3);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSitOn(level, pos);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess ticks,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighbor,
            RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            if (level instanceof Level concrete && !concrete.isClientSide()) {
                Block.popResource(concrete, pos, new ItemStack(this));
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighbor, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!seesSun(level, pos)) {
            return;
        }
        if (random.nextInt(WEATHER_ODDS) == 0) {
            tryWeather(level, pos);
        }
        if (random.nextInt(SPREAD_ODDS) == 0) {
            trySpread(level, pos, random);
        }
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!this.active || !stack.is(Items.SHEARS)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!level.isClientSide()) {
            shear(level, pos);
            stack.hurtAndBreak(1, player, hand);
        }
        return InteractionResult.SUCCESS;
    }
}
