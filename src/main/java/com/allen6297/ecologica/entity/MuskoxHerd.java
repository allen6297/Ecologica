package com.allen6297.ecologica.entity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Blocks;

/**
 * What a muskox does with the rest of the herd and with the lichen. The goals call these;
 * the game tests call them directly.
 */
public final class MuskoxHerd {
    /** Other muskox inside this box count as the same herd. */
    public static final double HERD_RADIUS = 8.0;
    /** Closer than this, an adult stops walking in. Farther, and it has lost the herd. */
    public static final double FOLLOW_NEAR = 6.0;
    public static final double FOLLOW_FAR = 18.0;
    /** A wolf this close to a calf puts the adults into a ring. */
    public static final double WOLF_NOTICE = 12.0;
    public static final double RING_RADIUS = 2.5;
    /** This many other muskox in the herd box is a pen, and a pen pays no qiviut. */
    public static final int CROWD = 4;

    private MuskoxHerd() {}

    public static boolean tryGraze(Muskox muskox) {
        Level level = muskox.level();
        BlockPos feet = muskox.blockPosition();
        BlockPos[] spots = {feet, feet.below(), feet.north(), feet.south(), feet.east(), feet.west()};
        for (BlockPos spot : spots) {
            if (level.getBlockState(spot).is(EcologicaBlocks.SPROUTED_GLOW_LICHEN.get())
                    || level.getBlockState(spot).is(EcologicaBlocks.SHEARED_GLOW_LICHEN.get())) {
                continue;
            }
            if (level.getBlockState(spot).is(Blocks.GLOW_LICHEN)) {
                level.removeBlock(spot, false);
                muskox.setFed(true);
                return true;
            }
        }
        return false;
    }

    public static int othersNearby(Muskox muskox) {
        AABB box = muskox.getBoundingBox().inflate(HERD_RADIUS);
        return muskox.level().getEntitiesOfClass(Muskox.class, box, other -> other != muskox && other.isAlive()).size();
    }

    /** Qiviut only from a muskox that has eaten and still has room. A crowded pen gives nothing. */
    public static ItemStack qiviutDrop(Muskox muskox) {
        if (!muskox.isFed() || othersNearby(muskox) >= CROWD) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(EcologicaItems.QIVIUT.get());
    }

    public static Optional<Muskox> herdMate(Muskox muskox) {
        if (muskox.isBaby()) {
            return Optional.empty();
        }
        AABB box = muskox.getBoundingBox().inflate(FOLLOW_FAR);
        return muskox.level().getEntitiesOfClass(Muskox.class, box, other -> other != muskox && other.isAlive() && !other.isBaby())
                .stream()
                .filter(other -> muskox.distanceToSqr(other) > FOLLOW_NEAR * FOLLOW_NEAR)
                .min(Comparator.comparingDouble(muskox::distanceToSqr));
    }

    /**
     * A slot on a circle around the nearest calf, once a wolf is close enough to matter.
     * Adults sort by id so two muskox do not pick the same place.
     */
    public static Optional<Vec3> ringSlot(Muskox muskox) {
        if (muskox.isBaby()) {
            return Optional.empty();
        }
        AABB box = muskox.getBoundingBox().inflate(WOLF_NOTICE);
        List<Muskox> calves = muskox.level().getEntitiesOfClass(Muskox.class, box, other -> other.isBaby() && other.isAlive());
        if (calves.isEmpty()) {
            return Optional.empty();
        }
        Muskox calf = calves.getFirst();
        boolean wolfNear = !muskox.level().getEntitiesOfClass(Wolf.class, calf.getBoundingBox().inflate(WOLF_NOTICE), Wolf::isAlive).isEmpty();
        if (!wolfNear) {
            return Optional.empty();
        }
        List<Muskox> adults = muskox.level().getEntitiesOfClass(Muskox.class, calf.getBoundingBox().inflate(WOLF_NOTICE), other -> !other.isBaby() && other.isAlive());
        adults.sort(Comparator.comparing(other -> other.getUUID().toString()));
        int index = Math.max(0, adults.indexOf(muskox));
        int count = Math.max(1, adults.size());
        double angle = (Math.PI * 2.0 * index) / count;
        Vec3 center = calf.position();
        return Optional.of(new Vec3(center.x + Math.cos(angle) * RING_RADIUS, center.y, center.z + Math.sin(angle) * RING_RADIUS));
    }
}
