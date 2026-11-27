package com.allen6297.ecologica.worldgen;

import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

/**
 * A smooth-basalt disk is the greenstone belt. Banded iron sits in that disk, one raw iron
 * block sits in the belt, and a short vein continues down into the deepslate under it.
 */
public record GreenstoneBeltFeature() implements Feature {
    public static final int RADIUS = 3;
    public static final int VEIN_DEPTH = 4;
    public static final MapCodec<GreenstoneBeltFeature> CODEC = MapCodec.unit(GreenstoneBeltFeature::new);

    @Override
    public MapCodec<GreenstoneBeltFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos surface = findSurface(level, origin);
        if (surface == null) {
            return false;
        }
        placeBelt(level, surface);
        return true;
    }

    /**
     * The heightmap may point at the ground or at the air and snow above it.
     * The belt only starts on stone or deepslate, which is what the column leaves behind.
     */
    public static BlockPos findSurface(LevelAccessor level, BlockPos origin) {
        if (isHost(level.getBlockState(origin))) {
            return origin;
        }
        BlockPos below = origin.below();
        if (isHost(level.getBlockState(below))) {
            return below;
        }
        return null;
    }

    public static void placeBelt(LevelAccessor level, BlockPos surface) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                if (dx * dx + dz * dz > RADIUS * RADIUS) {
                    continue;
                }
                for (int dy = 0; dy >= -1; dy--) {
                    pos.set(surface.getX() + dx, surface.getY() + dy, surface.getZ() + dz);
                    if (isHost(level.getBlockState(pos))) {
                        level.setBlock(pos, Blocks.SMOOTH_BASALT.defaultBlockState(), 2);
                    }
                }
            }
        }

        pos.set(surface);
        if (level.getBlockState(pos).is(Blocks.SMOOTH_BASALT)) {
            level.setBlock(pos, EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get().defaultBlockState(), 2);
        }

        BlockPos rawIron = surface.east();
        if (level.getBlockState(rawIron).is(Blocks.SMOOTH_BASALT)) {
            level.setBlock(rawIron, Blocks.RAW_IRON_BLOCK.defaultBlockState(), 2);
        }

        for (int dy = 1; dy <= VEIN_DEPTH; dy++) {
            pos.set(surface.getX(), surface.getY() - dy, surface.getZ());
            BlockState state = level.getBlockState(pos);
            if (!state.is(Blocks.DEEPSLATE) && !state.is(Blocks.SMOOTH_BASALT) && !state.is(Blocks.STONE)) {
                break;
            }
            level.setBlock(pos, EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get().defaultBlockState(), 2);
        }
    }

    private static boolean isHost(BlockState state) {
        return state.is(Blocks.DEEPSLATE) || state.is(Blocks.STONE);
    }
}
