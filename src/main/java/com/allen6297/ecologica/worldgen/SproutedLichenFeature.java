package com.allen6297.ecologica.worldgen;

import com.allen6297.ecologica.block.SproutedGlowLichenBlock;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

/**
 * One sprouted lichen on the rock. Snow at that spot is cleared; the rest of the snow stays.
 */
public record SproutedLichenFeature() implements Feature {
    public static final MapCodec<SproutedLichenFeature> CODEC = MapCodec.unit(SproutedLichenFeature::new);

    @Override
    public MapCodec<SproutedLichenFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos ground = origin;
        BlockState here = level.getBlockState(origin);
        if (!here.isFaceSturdy(level, origin, net.minecraft.core.Direction.UP)) {
            ground = origin.below();
        }
        BlockPos at = ground.above();
        BlockState occupied = level.getBlockState(at);
        if (!occupied.isAir() && !occupied.canBeReplaced()) {
            return false;
        }
        BlockState lichen = EcologicaBlocks.SPROUTED_GLOW_LICHEN.get().defaultBlockState();
        if (!SproutedGlowLichenBlock.canSitOn(level, at)) {
            return false;
        }
        level.setBlock(at, lichen, 2);
        return true;
    }
}
