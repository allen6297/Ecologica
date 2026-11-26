package com.allen6297.ecologica.block;

import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.tag.EcologicaTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

/**
 * Sphagnum-like skin on a fen. A patch of it slowly precipitates bog iron into the sediment
 * underneath. Strip the patch and the seep stops. A neighborhood that is already full of
 * concretions waits. Nothing punishes the player for digging; the seep simply has nothing left to do.
 */
public class FenMossBlock extends CarpetBlock {
    /** Horizontal distance, in blocks, that counts as one patch. */
    public static final int PATCH_RADIUS = 2;
    /** Moss blocks, including the one that ticked, before the seep will work. */
    public static final int MIN_PATCH = 4;
    /** Concretions already in the patch. At this count the seep pauses until some are dug. */
    public static final int MAX_ORE_IN_PATCH = 3;
    /** One in this many random ticks attempts to spread. */
    public static final int SPREAD_ODDS = 4;
    /** One in this many random ticks attempts to precipitate bog iron. */
    public static final int SEEP_ODDS = 10;

    public FenMossBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(EcologicaTags.Blocks.FEN_MOSS_SUPPORTS);
    }

    @Override
    protected BlockState updateShape(
            BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            // Digging the sediment out from under a transplant should hand the moss back, not delete it.
            if (level instanceof Level concrete && !concrete.isClientSide()) {
                Block.popResource(concrete, pos, new ItemStack(this));
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!isInFen(level, pos)) {
            return;
        }
        if (random.nextInt(SPREAD_ODDS) == 0) {
            trySpread(level, pos, random);
        }
        if (random.nextInt(SEEP_ODDS) == 0) {
            trySeep(level, pos, random);
        }
    }

    public static boolean isInFen(LevelReader level, BlockPos pos) {
        return level.getBiome(pos).is(EcologicaTags.Biomes.HAS_BOG_IRON);
    }

    /**
     * Moves onto a neighboring empty block whose floor is wetland sediment, and only inside an invited biome.
     *
     * @return true when a new moss block was placed
     */
    public static boolean trySpread(ServerLevel level, BlockPos origin, RandomSource random) {
        BlockPos target = origin.offset(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
        if (target.equals(origin) || !level.isEmptyBlock(target)) {
            return false;
        }
        if (!level.getBlockState(target.below()).is(EcologicaTags.Blocks.BOG_IRON_REPLACEABLES)) {
            return false;
        }
        if (!isInFen(level, target)) {
            return false;
        }
        level.setBlock(target, EcologicaBlocks.FEN_MOSS.get().defaultBlockState(), Block.UPDATE_ALL);
        return true;
    }

    /**
     * Precipitates one bog-iron concretion into sediment that still has moss on it.
     * A patch smaller than {@link #MIN_PATCH} does nothing. A patch that already holds
     * {@link #MAX_ORE_IN_PATCH} concretions waits.
     *
     * @return true when a sediment block became bog iron
     */
    public static boolean trySeep(ServerLevel level, BlockPos origin, RandomSource random) {
        if (count(level, origin, state -> state.is(EcologicaBlocks.FEN_MOSS.get())) < MIN_PATCH) {
            return false;
        }
        if (count(level, origin, state -> state.is(EcologicaBlocks.BOG_IRON_ORE.get())) >= MAX_ORE_IN_PATCH) {
            return false;
        }
        List<BlockPos> candidates = new ArrayList<>();
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -PATCH_RADIUS; dx <= PATCH_RADIUS; dx++) {
                for (int dz = -PATCH_RADIUS; dz <= PATCH_RADIUS; dz++) {
                    BlockPos moss = origin.offset(dx, dy, dz);
                    if (!level.getBlockState(moss).is(EcologicaBlocks.FEN_MOSS.get())) {
                        continue;
                    }
                    BlockPos sediment = moss.below();
                    if (level.getBlockState(sediment).is(EcologicaTags.Blocks.BOG_IRON_REPLACEABLES)) {
                        candidates.add(sediment);
                    }
                }
            }
        }
        if (candidates.isEmpty()) {
            return false;
        }
        BlockPos chosen = candidates.get(random.nextInt(candidates.size()));
        level.setBlock(chosen, EcologicaBlocks.BOG_IRON_ORE.get().defaultBlockState(), Block.UPDATE_ALL);
        return true;
    }

    private static int count(ServerLevel level, BlockPos origin, java.util.function.Predicate<BlockState> test) {
        int found = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -PATCH_RADIUS; dx <= PATCH_RADIUS; dx++) {
                for (int dz = -PATCH_RADIUS; dz <= PATCH_RADIUS; dz++) {
                    if (test.test(level.getBlockState(origin.offset(dx, dy, dz)))) {
                        found++;
                    }
                }
            }
        }
        return found;
    }
}
