package com.allen6297.ecologica.block;

import com.allen6297.ecologica.registry.EcologicaBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

/**
 * Organic mud built up under a living fen. It burns, poorly. Left open to the air, with no moss
 * and no water against it, it dries into a crust that the seep will not use.
 */
public class PeatBlock extends Block {
    /** Four smelts. A plank is one and a half, coal is eight. */
    public static final int BURN_TIME = 800;

    public PeatBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        tryDry(level, pos);
    }

    /**
     * @return true when this peat became dried fen soil
     */
    public static boolean tryDry(ServerLevel level, BlockPos pos) {
        if (!level.getBlockState(pos).is(EcologicaBlocks.PEAT.get())) {
            return false;
        }
        if (isKeptWet(level, pos)) {
            return false;
        }
        level.setBlock(pos, EcologicaBlocks.DRIED_FEN_SOIL.get().defaultBlockState(), Block.UPDATE_ALL);
        return true;
    }

    /**
     * Moss keeps the peat wet. Water against the block does too. Vanilla mud is not dried by this;
     * only peat, the fen's own organic layer, can crust over.
     */
    public static boolean isKeptWet(LevelReader level, BlockPos pos) {
        if (level.getBlockState(pos.above()).is(EcologicaBlocks.FEN_MOSS.get())) {
            return true;
        }
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
