package com.allen6297.ecologica.registry;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.block.FenMossBlock;

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
    public static final DeferredBlock<DropExperienceBlock> BOG_IRON_ORE = BLOCKS.register("bog_iron_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(0, 1),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_ORANGE)
                            .strength(1.25F)
                            .sound(SoundType.PACKED_MUD)
                            .requiresCorrectToolForDrops()));

    /**
     * A living skin on wetland sediment. Four or more in a small patch slowly turn the
     * mud under them into bog iron. Fewer than that, or a patch that is already full, does not.
     */
    public static final DeferredBlock<FenMossBlock> FEN_MOSS = BLOCKS.register("fen_moss",
            () -> new FenMossBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(0.1F)
                    .sound(SoundType.MOSS_CARPET)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()
                    .randomTicks()));

    private EcologicaBlocks() {}
}
