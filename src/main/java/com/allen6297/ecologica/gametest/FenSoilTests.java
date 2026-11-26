package com.allen6297.ecologica.gametest;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.block.DriedFenSoilBlock;
import com.allen6297.ecologica.block.FenMossBlock;
import com.allen6297.ecologica.block.PeatBlock;
import com.allen6297.ecologica.registry.EcologicaBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

/**
 * Peat is the step between mineral mud and bog iron, and it is the part of the fen that can dry out.
 */
@GameTestHolder(Ecologica.MOD_ID)
@PrefixGameTestTemplate(false)
public class FenSoilTests {
    private static final BlockPos PATCH = new BlockPos(3, 2, 3);

    @GameTest(template = "platform")
    public void livingPatchTurnsMudIntoPeat(GameTestHelper helper) {
        layOn(helper, Blocks.MUD);
        boolean formed = FenMossBlock.tryAccumulate(helper.getLevel(), helper.absolutePos(PATCH), RandomSource.create(7));
        if (!formed) {
            helper.fail("a living patch should build peat out of the mud under it");
        }
        int peat = countFloor(helper, state -> state.is(EcologicaBlocks.PEAT.get()));
        if (peat != 1) {
            helper.fail("expected one peat block, found " + peat);
        }
        helper.assertBlockPresent(EcologicaBlocks.FEN_MOSS.get(), PATCH);
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void mudDoesNotBecomeBogIronDirectly(GameTestHelper helper) {
        layOn(helper, Blocks.MUD);
        boolean formed = FenMossBlock.trySeep(helper.getLevel(), helper.absolutePos(PATCH), RandomSource.create(8));
        if (formed) {
            helper.fail("bog iron should precipitate into peat, not into bare mud");
        }
        helper.assertBlockPresent(Blocks.MUD, PATCH.below());
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void smallPatchDoesNotAccumulate(GameTestHelper helper) {
        helper.setBlock(PATCH.below(), Blocks.MUD);
        helper.setBlock(PATCH, EcologicaBlocks.FEN_MOSS.get());
        boolean formed = FenMossBlock.tryAccumulate(helper.getLevel(), helper.absolutePos(PATCH), RandomSource.create(9));
        if (formed) {
            helper.fail("one moss block is not enough to build peat");
        }
        helper.assertBlockPresent(Blocks.MUD, PATCH.below());
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void exposedPeatDries(GameTestHelper helper) {
        BlockPos peat = new BlockPos(3, 1, 3);
        helper.setBlock(peat, EcologicaBlocks.PEAT.get());
        if (!PeatBlock.tryDry(helper.getLevel(), helper.absolutePos(peat))) {
            helper.fail("peat left open to the air should dry");
        }
        helper.assertBlockPresent(EcologicaBlocks.DRIED_FEN_SOIL.get(), peat);
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void mossKeepsPeatFromDrying(GameTestHelper helper) {
        BlockPos peat = PATCH.below();
        helper.setBlock(peat, EcologicaBlocks.PEAT.get());
        helper.setBlock(PATCH, EcologicaBlocks.FEN_MOSS.get());
        if (PeatBlock.tryDry(helper.getLevel(), helper.absolutePos(peat))) {
            helper.fail("peat under fen moss should stay wet");
        }
        helper.assertBlockPresent(EcologicaBlocks.PEAT.get(), peat);
        helper.assertBlockPresent(EcologicaBlocks.FEN_MOSS.get(), PATCH);
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void waterKeepsPeatFromDrying(GameTestHelper helper) {
        BlockPos peat = new BlockPos(3, 1, 3);
        helper.setBlock(peat, EcologicaBlocks.PEAT.get());
        helper.setBlock(peat.east(), Blocks.WATER);
        if (PeatBlock.tryDry(helper.getLevel(), helper.absolutePos(peat))) {
            helper.fail("peat with water against it should stay peat");
        }
        helper.assertBlockPresent(EcologicaBlocks.PEAT.get(), peat);
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void waterTurnsDriedSoilBackIntoMud(GameTestHelper helper) {
        BlockPos soil = new BlockPos(3, 1, 3);
        helper.setBlock(soil, EcologicaBlocks.DRIED_FEN_SOIL.get());
        if (DriedFenSoilBlock.tryRewet(helper.getLevel(), helper.absolutePos(soil))) {
            helper.fail("dried fen soil with no water should stay dry");
        }
        helper.setBlock(soil.north(), Blocks.WATER);
        if (!DriedFenSoilBlock.tryRewet(helper.getLevel(), helper.absolutePos(soil))) {
            helper.fail("water against dried fen soil should turn it back into mud");
        }
        helper.assertBlockPresent(Blocks.MUD, soil);
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void mossPopsOffDriedSoil(GameTestHelper helper) {
        BlockPos soil = PATCH.below();
        helper.setBlock(soil, EcologicaBlocks.PEAT.get());
        helper.setBlock(PATCH, EcologicaBlocks.FEN_MOSS.get());
        helper.setBlock(soil, EcologicaBlocks.DRIED_FEN_SOIL.get());
        helper.assertBlockPresent(Blocks.AIR, PATCH);
        helper.succeed();
    }

    @GameTest(template = "platform")
    public void peatBurnsLongerThanAPlankAndShorterThanCoal(GameTestHelper helper) {
        FurnaceFuel fuel = EcologicaBlocks.PEAT.get().asItem().builtInRegistryHolder().getData(NeoForgeDataMaps.FURNACE_FUELS);
        if (fuel == null) {
            helper.fail("peat should be registered as a furnace fuel");
            return;
        }
        if (fuel.burnTime() != PeatBlock.BURN_TIME) {
            helper.fail("peat burn time should be " + PeatBlock.BURN_TIME + ", was " + fuel.burnTime());
        }
        helper.succeed();
    }

    private static void layOn(GameTestHelper helper, net.minecraft.world.level.block.Block floor) {
        BlockPos[] mosses = {PATCH, PATCH.east(), PATCH.south(), PATCH.east().south()};
        for (BlockPos moss : mosses) {
            helper.setBlock(moss.below(), floor);
            helper.setBlock(moss, EcologicaBlocks.FEN_MOSS.get());
        }
    }

    private static int countFloor(GameTestHelper helper, java.util.function.Predicate<net.minecraft.world.level.block.state.BlockState> test) {
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
}
