package com.allen6297.ecologica.gametest;

import com.allen6297.ecologica.block.SproutedGlowLichenBlock;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.worldgen.GreenstoneBeltFeature;
import com.allen6297.ecologica.worldgen.TundraColumnFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

/**
 * The tundra column, the greenstone belt, and the lichen that eats the rock.
 */
public class TundraTests {
    public static void columnTurnsSoilAndStoneIntoDeepslate(GameTestHelper helper) {
        BlockPos grass = new BlockPos(1, 1, 1);
        helper.setBlock(grass, Blocks.GRASS_BLOCK);
        helper.setBlock(grass.above(), Blocks.DIRT);
        helper.setBlock(grass.above(2), Blocks.STONE);
        BlockPos bottom = helper.absolutePos(grass);
        TundraColumnFeature.rewrite(helper.getLevel(), bottom, bottom.getY(), bottom.getY() + 2);
        helper.assertBlockPresent(Blocks.DEEPSLATE, grass);
        helper.assertBlockPresent(Blocks.DEEPSLATE, grass.above());
        helper.assertBlockPresent(Blocks.DEEPSLATE, grass.above(2));
        helper.succeed();
    }

    public static void columnLeavesWaterAndWood(GameTestHelper helper) {
        BlockPos water = new BlockPos(2, 1, 1);
        helper.setBlock(water, Blocks.WATER);
        helper.setBlock(water.above(), Blocks.OAK_LOG);
        BlockPos bottom = helper.absolutePos(water);
        TundraColumnFeature.rewrite(helper.getLevel(), bottom, bottom.getY(), bottom.getY() + 1);
        helper.assertBlockPresent(Blocks.WATER, water);
        helper.assertBlockPresent(Blocks.OAK_LOG, water.above());
        helper.succeed();
    }

    public static void beltSetsGreenstoneOreAndAVein(GameTestHelper helper) {
        for (int x = 0; x <= 6; x++) {
            for (int z = 0; z <= 6; z++) {
                helper.setBlock(new BlockPos(x, 1, z), Blocks.DEEPSLATE);
            }
        }
        BlockPos center = new BlockPos(3, 1, 3);
        helper.setBlock(center.below(), Blocks.DEEPSLATE);
        GreenstoneBeltFeature.placeBelt(helper.getLevel(), helper.absolutePos(center));
        helper.assertBlockPresent(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get(), center);
        helper.assertBlockPresent(Blocks.RAW_IRON_BLOCK, center.east());
        helper.assertBlockPresent(Blocks.SMOOTH_BASALT, center.north());
        helper.assertBlockPresent(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get(), center.below());
        helper.succeed();
    }

    public static void stoneWeathersToGravel(GameTestHelper helper) {
        BlockPos lichen = new BlockPos(3, 2, 3);
        helper.setBlock(lichen.below(), Blocks.STONE);
        helper.setBlock(lichen, EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        if (!SproutedGlowLichenBlock.tryWeather(helper.getLevel(), helper.absolutePos(lichen))) {
            helper.fail("stone under sprouted lichen should weather");
        }
        helper.assertBlockPresent(Blocks.GRAVEL, lichen.below());
        helper.succeed();
    }

    public static void gravelWeathersToSand(GameTestHelper helper) {
        BlockPos lichen = new BlockPos(3, 2, 3);
        helper.setBlock(lichen.below(), Blocks.GRAVEL);
        helper.setBlock(lichen, EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        SproutedGlowLichenBlock.tryWeather(helper.getLevel(), helper.absolutePos(lichen));
        helper.assertBlockPresent(Blocks.SAND, lichen.below());
        helper.succeed();
    }

    public static void sandWeathersToSoil(GameTestHelper helper) {
        BlockPos lichen = new BlockPos(3, 2, 3);
        helper.setBlock(lichen.below(), Blocks.SAND);
        helper.setBlock(lichen, EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        SproutedGlowLichenBlock.tryWeather(helper.getLevel(), helper.absolutePos(lichen));
        helper.assertBlockPresent(Blocks.DIRT, lichen.below());
        helper.succeed();
    }

    public static void mudDoesNotWeather(GameTestHelper helper) {
        BlockPos lichen = new BlockPos(3, 2, 3);
        helper.setBlock(lichen.below(), Blocks.MUD);
        helper.setBlock(lichen, EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        if (SproutedGlowLichenBlock.tryWeather(helper.getLevel(), helper.absolutePos(lichen))) {
            helper.fail("mud is not the lichen's substrate");
        }
        helper.assertBlockPresent(Blocks.MUD, lichen.below());
        helper.succeed();
    }

    public static void shearsHaltWeathering(GameTestHelper helper) {
        BlockPos lichen = new BlockPos(3, 2, 3);
        helper.setBlock(lichen.below(), Blocks.STONE);
        helper.setBlock(lichen, EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        SproutedGlowLichenBlock.shear(helper.getLevel(), helper.absolutePos(lichen));
        helper.assertBlockPresent(EcologicaBlocks.SHEARED_GLOW_LICHEN.get(), lichen);
        if (SproutedGlowLichenBlock.tryWeather(helper.getLevel(), helper.absolutePos(lichen))) {
            helper.fail("sheared lichen should leave the stone alone");
        }
        helper.assertBlockPresent(Blocks.STONE, lichen.below());
        helper.succeed();
    }

    public static void shadeHaltsWeathering(GameTestHelper helper) {
        BlockPos lichen = new BlockPos(3, 2, 3);
        helper.setBlock(lichen.below(), Blocks.STONE);
        helper.setBlock(lichen, EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        helper.setBlock(lichen.above(), Blocks.STONE);
        if (SproutedGlowLichenBlock.seesSun(helper.getLevel(), helper.absolutePos(lichen))) {
            helper.fail("a block overhead should keep the lichen from working");
        }
        helper.setBlock(lichen.above(), Blocks.AIR);
        if (!SproutedGlowLichenBlock.seesSun(helper.getLevel(), helper.absolutePos(lichen))) {
            helper.fail("open sky should let the lichen work");
        }
        helper.succeed();
    }

    public static void lichenSpreadsWithinEight(GameTestHelper helper) {
        BlockPos origin = new BlockPos(1, 2, 1);
        BlockPos target = new BlockPos(1, 2, 4);
        helper.setBlock(origin.below(), Blocks.DEEPSLATE);
        helper.setBlock(origin, EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        helper.setBlock(target.below(), Blocks.STONE);
        helper.setBlock(target, Blocks.AIR);
        if (!SproutedGlowLichenBlock.spreadToward(helper.getLevel(), helper.absolutePos(origin), helper.absolutePos(target))) {
            helper.fail("ordinary lichen should spread a few blocks onto open rock");
        }
        helper.assertBlockPresent(Blocks.GLOW_LICHEN, target);
        helper.succeed();
    }

    public static void lichenDoesNotSpreadPastEight(GameTestHelper helper) {
        BlockPos origin = helper.absolutePos(new BlockPos(1, 2, 1));
        BlockPos target = origin.offset(0, 0, SproutedGlowLichenBlock.SPREAD_RANGE + 1);
        if (SproutedGlowLichenBlock.spreadToward(helper.getLevel(), origin, target)) {
            helper.fail("spread should stop at about eight blocks");
        }
        helper.succeed();
    }

    public static void bonemealSproutsGlowLichen(GameTestHelper helper) {
        BlockPos lichen = new BlockPos(3, 2, 3);
        helper.setBlock(lichen.below(), Blocks.STONE);
        helper.setBlock(lichen, Blocks.GLOW_LICHEN);
        SproutedGlowLichenBlock.onBonemeal(new net.neoforged.neoforge.event.entity.player.BonemealEvent(
                null,
                helper.getLevel(),
                helper.absolutePos(lichen),
                helper.getBlockState(lichen),
                net.minecraft.world.level.block.BonemealSource.INTERACTION,
                net.minecraft.world.item.Items.BONE_MEAL.getDefaultInstance()));
        helper.assertBlockPresent(EcologicaBlocks.SPROUTED_GLOW_LICHEN.get(), lichen);
        helper.succeed();
    }
}
