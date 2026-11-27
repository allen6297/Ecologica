package com.allen6297.ecologica.gametest;

import com.allen6297.ecologica.entity.Muskox;
import com.allen6297.ecologica.entity.MuskoxHerd;
import com.allen6297.ecologica.entity.PredatorSatiation;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaEntities;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * Grazing, the calf ring, and a wolf that has already eaten.
 */
public class MuskoxTests {
    public static void grazesUnsproutedLichen(GameTestHelper helper) {
        Muskox muskox = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(3, 2, 3));
        helper.setBlock(new BlockPos(3, 2, 3), Blocks.GLOW_LICHEN);
        if (!MuskoxHerd.tryGraze(muskox)) {
            helper.fail("a muskox should eat ordinary glow lichen");
        }
        if (!muskox.isFed()) {
            helper.fail("grazing should leave the muskox fed");
        }
        helper.assertBlockPresent(Blocks.AIR, new BlockPos(3, 2, 3));
        helper.succeed();
    }

    public static void doesNotEatSproutedLichen(GameTestHelper helper) {
        Muskox muskox = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(3, 2, 3));
        helper.setBlock(new BlockPos(3, 2, 2), Blocks.DEEPSLATE);
        helper.setBlock(new BlockPos(3, 3, 2), EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        if (MuskoxHerd.tryGraze(muskox)) {
            helper.fail("sprouted lichen is the pioneer that makes soil, and the herd leaves it");
        }
        helper.assertBlockPresent(EcologicaBlocks.SPROUTED_GLOW_LICHEN.get(), new BlockPos(3, 3, 2));
        helper.succeed();
    }

    public static void ringFormsAroundACalf(GameTestHelper helper) {
        Muskox calf = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(3, 2, 3));
        calf.setBaby(true);
        Muskox adult = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(2, 2, 3));
        helper.spawnWithNoFreeWill(EntityTypes.WOLF, new BlockPos(5, 2, 3));
        Vec3 slot = MuskoxHerd.ringSlot(adult).orElse(null);
        if (slot == null) {
            helper.fail("an adult should take a place around the calf when a wolf is close");
            return;
        }
        double distance = calf.position().distanceTo(slot);
        if (distance < 2.0 || distance > 3.2) {
            helper.fail("the ring should stand about two and a half blocks out, was " + distance);
        }
        helper.succeed();
    }

    public static void ringWaitsWithoutAWolf(GameTestHelper helper) {
        Muskox calf = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(3, 2, 3));
        calf.setBaby(true);
        Muskox adult = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(2, 2, 3));
        if (MuskoxHerd.ringSlot(adult).isPresent()) {
            helper.fail("without a wolf the herd should keep grazing, not form a ring");
        }
        helper.succeed();
    }

    public static void fedMuskoxGivesQiviut(GameTestHelper helper) {
        Muskox muskox = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(3, 2, 3));
        muskox.setFed(true);
        ItemStack drop = MuskoxHerd.qiviutDrop(muskox);
        if (!drop.is(EcologicaItems.QIVIUT.get())) {
            helper.fail("a fed muskox with room around it should give qiviut");
        }
        helper.succeed();
    }

    public static void crowdedHerdGivesNoQiviut(GameTestHelper helper) {
        Muskox fed = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(3, 2, 3));
        fed.setFed(true);
        helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(3, 2, 2));
        helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(3, 2, 4));
        helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(2, 2, 3));
        helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(4, 2, 3));
        if (!MuskoxHerd.qiviutDrop(fed).isEmpty()) {
            helper.fail("a packed herd gives nothing");
        }
        helper.succeed();
    }

    public static void fedWolfDropsTheHunt(GameTestHelper helper) {
        Wolf wolf = helper.spawnWithNoFreeWill(EntityTypes.WOLF, new BlockPos(2, 2, 2));
        Muskox muskox = helper.spawnWithNoFreeWill(EcologicaEntities.MUSKOX.get(), new BlockPos(4, 2, 2));
        wolf.setTarget(muskox);
        if (wolf.getTarget() != muskox) {
            helper.fail("a hungry wolf should be able to mark a muskox");
            return;
        }
        PredatorSatiation.markFed(wolf);
        wolf.setTarget(muskox);
        if (wolf.getTarget() != null) {
            helper.fail("a wolf that just fed should let go of the herd");
        }
        if (!PredatorSatiation.isSated(wolf)) {
            helper.fail("the wolf should stay full");
        }
        helper.succeed();
    }
}
