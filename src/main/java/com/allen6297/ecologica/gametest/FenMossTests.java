package com.allen6297.ecologica.gametest;

import java.util.List;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.block.FenMossBlock;
import com.allen6297.ecologica.registry.EcologicaBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The fen loop, driven directly so the test does not wait on random ticks.
 * The structure is a stone slab; each test lays its own mud.
 */
@GameTestHolder(Ecologica.MOD_ID)
@PrefixGameTestTemplate(false)
public class FenMossTests {
    private static final BlockPos PATCH = new BlockPos(3, 2, 3);

    @GameTest(template = "platform")
    public void seepFormsOneConcretionAndLeavesTheMoss(GameTestHelper helper) {
        layPatch(helper);
        ServerLevel level = helper.getLevel();
        boolean formed = FenMossBlock.trySeep(level, helper.absolutePos(PATCH), RandomSource.create(1));
        if (!formed) {
            helper.fail("a patch of fen moss should precipitate bog iron");
        }
        int ore = countRelative(helper, stateIsOre());
        if (ore != 1) {
            helper.fail("expected one new concretion, found " + ore);
        }
        assertMossStillThere(helper);
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void seepDoesNothingBelowThePatchSize(GameTestHelper helper) {
        helper.setBlock(PATCH.below(), EcologicaBlocks.PEAT.get());
        helper.setBlock(PATCH, EcologicaBlocks.FEN_MOSS.get());
        helper.setBlock(PATCH.east().below(), EcologicaBlocks.PEAT.get());
        helper.setBlock(PATCH.east(), EcologicaBlocks.FEN_MOSS.get());
        boolean formed = FenMossBlock.trySeep(helper.getLevel(), helper.absolutePos(PATCH), RandomSource.create(2));
        if (formed) {
            helper.fail("two moss blocks are not a working patch");
        }
        helper.assertBlockPresent(EcologicaBlocks.PEAT.get(), PATCH.below());
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void seepPausesWhenTheNeighborhoodIsFull(GameTestHelper helper) {
        layPatch(helper);
        // Three concretions already in the patch, none of them under the moss, so there is still sediment to convert.
        helper.setBlock(new BlockPos(1, 1, 1), EcologicaBlocks.BOG_IRON_ORE.get());
        helper.setBlock(new BlockPos(2, 1, 1), EcologicaBlocks.BOG_IRON_ORE.get());
        helper.setBlock(new BlockPos(3, 1, 1), EcologicaBlocks.BOG_IRON_ORE.get());
        boolean formed = FenMossBlock.trySeep(helper.getLevel(), helper.absolutePos(PATCH), RandomSource.create(3));
        if (formed) {
            helper.fail("a full seep should wait until a concretion is dug");
        }
        helper.assertBlockPresent(EcologicaBlocks.PEAT.get(), PATCH.below());
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void strippingThePatchStopsTheSeep(GameTestHelper helper) {
        layPatch(helper);
        helper.setBlock(PATCH.east(), Blocks.AIR);
        helper.setBlock(PATCH.south(), Blocks.AIR);
        helper.setBlock(PATCH.east().south(), Blocks.AIR);
        boolean formed = FenMossBlock.trySeep(helper.getLevel(), helper.absolutePos(PATCH), RandomSource.create(4));
        if (formed) {
            helper.fail("stripping the patch should stop new bog iron");
        }
        helper.assertBlockPresent(EcologicaBlocks.PEAT.get(), PATCH.below());
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void diggingTheSedimentReturnsTheMoss(GameTestHelper helper) {
        helper.setBlock(PATCH.below(), Blocks.MUD);
        helper.setBlock(PATCH, EcologicaBlocks.FEN_MOSS.get());
        helper.setBlock(PATCH.below(), Blocks.STONE);
        helper.assertBlockPresent(Blocks.AIR, PATCH);
        AABB box = new AABB(helper.absolutePos(PATCH)).inflate(1.0);
        List<ItemEntity> drops = helper.getLevel().getEntitiesOfClass(ItemEntity.class, box);
        boolean returned = drops.stream().anyMatch(entity -> entity.getItem().is(EcologicaBlocks.FEN_MOSS.get().asItem()));
        if (!returned) {
            helper.fail("fen moss should drop when its sediment is dug, so it can be replanted");
        }
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void spreadFollowsTheFenTag(GameTestHelper helper) {
        BlockPos neighbor = PATCH.east();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos floor = PATCH.offset(dx, -1, dz);
                helper.setBlock(floor, Blocks.MUD);
            }
        }
        helper.setBlock(PATCH, EcologicaBlocks.FEN_MOSS.get());
        ServerLevel level = helper.getLevel();
        boolean spread = false;
        RandomSource random = RandomSource.create(5);
        for (int attempt = 0; attempt < 40 && !spread; attempt++) {
            spread = FenMossBlock.trySpread(level, helper.absolutePos(PATCH), random);
        }
        boolean invited = FenMossBlock.isInFen(level, helper.absolutePos(neighbor));
        if (invited && !spread) {
            helper.fail("fen moss should spread onto nearby sediment inside an invited biome");
        }
        if (!invited && spread) {
            helper.fail("fen moss should not colonize a biome that has not been invited");
        }
        helper.succeed();
    }

    private static void layPatch(GameTestHelper helper) {
        BlockPos[] floor = {PATCH, PATCH.east(), PATCH.south(), PATCH.east().south()};
        for (BlockPos moss : floor) {
            helper.setBlock(moss.below(), EcologicaBlocks.PEAT.get());
            helper.setBlock(moss, EcologicaBlocks.FEN_MOSS.get());
        }
    }

    private static void assertMossStillThere(GameTestHelper helper) {
        helper.assertBlockPresent(EcologicaBlocks.FEN_MOSS.get(), PATCH);
        helper.assertBlockPresent(EcologicaBlocks.FEN_MOSS.get(), PATCH.east());
        helper.assertBlockPresent(EcologicaBlocks.FEN_MOSS.get(), PATCH.south());
        helper.assertBlockPresent(EcologicaBlocks.FEN_MOSS.get(), PATCH.east().south());
    }

    private static int countRelative(GameTestHelper helper, java.util.function.Predicate<net.minecraft.world.level.block.state.BlockState> test) {
        int found = 0;
        for (int x = 1; x <= 5; x++) {
            for (int z = 1; z <= 5; z++) {
                if (test.test(helper.getBlockState(new BlockPos(x, 1, z)))) {
                    found++;
                }
            }
        }
        return found;
    }

    private static java.util.function.Predicate<net.minecraft.world.level.block.state.BlockState> stateIsOre() {
        return state -> state.is(EcologicaBlocks.BOG_IRON_ORE.get());
    }
}
