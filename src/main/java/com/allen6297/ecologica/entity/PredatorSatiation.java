package com.allen6297.ecologica.entity;

import com.allen6297.ecologica.Ecologica;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * A wolf that has killed a muskox stops taking animal targets for two Minecraft days.
 * The herd gets that time. Nothing punishes the wolf; it is simply not hungry.
 */
public final class PredatorSatiation {
    /** Two Minecraft days. */
    public static final int FULL_TICKS = 24000 * 2;

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Ecologica.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> FED_UNTIL = ATTACHMENTS.register(
            "wolf_fed_until",
            () -> AttachmentType.builder(() -> 0L).serialize(com.mojang.serialization.Codec.LONG.fieldOf("fed_until")).build());

    private PredatorSatiation() {}

    public static void onWolfJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Wolf wolf)) {
            return;
        }
        wolf.targetSelector.addGoal(
                5,
                new NearestAttackableTargetGoal<>(wolf, Muskox.class, 10, true, false, (target, level) -> !isSated(wolf)));
    }

    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Wolf wolf) || !(event.getEntity() instanceof Muskox)) {
            return;
        }
        markFed(wolf);
    }

    public static void onRetarget(LivingChangeTargetEvent event) {
        if (!(event.getEntity() instanceof Wolf wolf) || !isSated(wolf)) {
            return;
        }
        if (event.getNewAboutToBeSetTarget() instanceof Animal) {
            event.setNewAboutToBeSetTarget(null);
        }
    }

    public static void markFed(Wolf wolf) {
        wolf.setData(FED_UNTIL.get(), wolf.level().getGameTime() + FULL_TICKS);
        wolf.setTarget(null);
    }

    public static boolean isSated(Wolf wolf) {
        return wolf.getData(FED_UNTIL.get()) > wolf.level().getGameTime();
    }
}
