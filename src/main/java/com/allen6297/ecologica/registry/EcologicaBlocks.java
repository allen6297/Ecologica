package com.allen6297.ecologica.registry;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.block.DriedFenSoilBlock;
import com.allen6297.ecologica.block.FenMossBlock;
import com.allen6297.ecologica.block.PeatBlock;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EcologicaBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Ecologica.MOD_ID);

    /**
     * A soft iron concretion in wetland mud. It is shoveled, not pickaxed:
     * the host material is soil, and the drop is raw bog iron rather than the block.
     */
    public static final DeferredBlock<DropExperienceBlock> BOG_IRON_ORE = BLOCKS.registerBlock(
            "bog_iron_ore",
            properties -> new DropExperienceBlock(UniformInt.of(0, 1), properties),
            properties -> properties
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(1.25F)
                    .sound(SoundType.PACKED_MUD)
                    .requiresCorrectToolForDrops());

    /**
     * A living skin on wetland sediment. Four or more in a small patch slowly turn the
     * mud under them into bog iron. Fewer than that, or a patch that is already full, does not.
     */
    public static final DeferredBlock<FenMossBlock> FEN_MOSS = BLOCKS.registerBlock(
            "fen_moss",
            FenMossBlock::new,
            properties -> properties
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(0.1F)
                    .sound(SoundType.MOSS_CARPET)
                    .pushReaction(PushReaction.POPPED)
                    .noOcclusion()
                    .randomTicks());

    /** Organic layer of a living fen. Shovel it for a poor fuel. Leave it uncovered and it dries. */
    public static final DeferredBlock<PeatBlock> PEAT = BLOCKS.registerBlock(
            "peat",
            PeatBlock::new,
            properties -> properties
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .strength(0.6F)
                    .sound(SoundType.MUD)
                    .randomTicks());

    /** Peat that lost its moss and its water. Water against it turns it back into mud. */
    public static final DeferredBlock<DriedFenSoilBlock> DRIED_FEN_SOIL = BLOCKS.registerBlock(
            "dried_fen_soil",
            DriedFenSoilBlock::new,
            properties -> properties
                    .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)
                    .randomTicks());

    private EcologicaBlocks() {}
}
