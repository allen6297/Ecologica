package com.allen6297.ecologica.block;

import com.allen6297.ecologica.registry.EcologicaBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Peat that lost its cover and its water. It is not a punishment: put water against it and it
 * becomes mud again, which a fen moss patch can turn back into peat.
 */
public class DriedFenSoilBlock extends Block {
    public DriedFenSoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        tryRewet(level, pos);
    }

    /**
     * @return true when this crust became mud
     */
    public static boolean tryRewet(ServerLevel level, BlockPos pos) {
        if (!level.getBlockState(pos).is(EcologicaBlocks.DRIED_FEN_SOIL.get())) {
            return false;
        }
        if (!touchesWater(level, pos)) {
            return false;
        }
        level.setBlock(pos, Blocks.MUD.defaultBlockState(), Block.UPDATE_ALL);
        return true;
    }

    public static boolean touchesWater(LevelReader level, BlockPos pos) {
        if (!level.getFluidState(pos.above()).isEmpty()) {
            return true;
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (!level.getFluidState(pos.relative(direction)).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
