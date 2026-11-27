package com.allen6297.ecologica.worldgen;

import com.allen6297.ecologica.tag.EcologicaTags;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;

/**
 * Deepslate from just above bedrock to the surface, but only for the soil and stone the tag names.
 * Water, ice, logs, and anything a pack did not list stay where they are.
 */
public record TundraColumnFeature() implements Feature {
    public static final MapCodec<TundraColumnFeature> CODEC = MapCodec.unit(TundraColumnFeature::new);

    @Override
    public MapCodec<TundraColumnFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        ChunkPos chunk = new ChunkPos(origin.getX() >> 4, origin.getZ() >> 4);
        int fromY = level.getMinY() + 1;
        boolean any = false;
        for (int x = chunk.getMinBlockX(); x <= chunk.getMaxBlockX(); x++) {
            for (int z = chunk.getMinBlockZ(); z <= chunk.getMaxBlockZ(); z++) {
                int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
                if (top >= fromY && rewrite(level, new BlockPos(x, 0, z), fromY, top)) {
                    any = true;
                }
            }
        }
        return any;
    }

    /**
     * @return whether any block in the inclusive range changed
     */
    public static boolean rewrite(LevelAccessor level, BlockPos column, int fromY, int toY) {
        boolean any = false;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = fromY; y <= toY; y++) {
            pos.set(column.getX(), y, column.getZ());
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.DEEPSLATE) || !state.is(EcologicaTags.Blocks.TUNDRA_COLUMN_REPLACEABLES)) {
                continue;
            }
            level.setBlock(pos, Blocks.DEEPSLATE.defaultBlockState(), 2);
            any = true;
        }
        return any;
    }
}
