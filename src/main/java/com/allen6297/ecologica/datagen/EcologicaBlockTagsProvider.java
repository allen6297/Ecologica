package com.allen6297.ecologica.datagen;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.tag.EcologicaTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class EcologicaBlockTagsProvider extends BlockTagsProvider {
    public EcologicaBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Ecologica.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                key(EcologicaBlocks.BOG_IRON_ORE.get()),
                key(EcologicaBlocks.PEAT.get()),
                key(EcologicaBlocks.DRIED_FEN_SOIL.get()));
        tag(Tags.Blocks.ORES_IRON).add(key(EcologicaBlocks.BOG_IRON_ORE.get()));
        tag(Tags.Blocks.ORE_RATES_SINGULAR).add(key(EcologicaBlocks.BOG_IRON_ORE.get()));
        tag(EcologicaTags.Blocks.BOG_IRON_REPLACEABLES).add(
                key(Blocks.DIRT),
                key(Blocks.COARSE_DIRT),
                key(Blocks.ROOTED_DIRT),
                key(Blocks.MUD),
                key(Blocks.CLAY),
                key(EcologicaBlocks.PEAT.get()));
        tag(EcologicaTags.Blocks.FEN_MOSS_SUPPORTS)
                .addTag(EcologicaTags.Blocks.BOG_IRON_REPLACEABLES)
                .add(key(EcologicaBlocks.BOG_IRON_ORE.get()));

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(key(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get()));
        tag(BlockTags.NEEDS_STONE_TOOL).add(key(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get()));
        tag(Tags.Blocks.ORES_IRON).add(key(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get()));
        tag(Tags.Blocks.ORE_RATES_DENSE).add(key(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get()));

        tag(EcologicaTags.Blocks.TUNDRA_COLUMN_REPLACEABLES).add(
                key(Blocks.GRASS_BLOCK),
                key(Blocks.DIRT),
                key(Blocks.COARSE_DIRT),
                key(Blocks.PODZOL),
                key(Blocks.ROOTED_DIRT),
                key(Blocks.DIRT_PATH),
                key(Blocks.STONE),
                key(Blocks.ANDESITE),
                key(Blocks.DIORITE),
                key(Blocks.GRANITE),
                key(Blocks.TUFF));
        tag(EcologicaTags.Blocks.LICHEN_SUPPORTS).add(
                key(Blocks.STONE),
                key(Blocks.DEEPSLATE),
                key(Blocks.SMOOTH_BASALT),
                key(Blocks.COBBLESTONE),
                key(Blocks.TUFF),
                key(Blocks.ANDESITE),
                key(Blocks.DIORITE),
                key(Blocks.GRANITE),
                key(Blocks.GRAVEL),
                key(Blocks.SAND),
                key(Blocks.DIRT),
                key(Blocks.MUD));
    }

    private static ResourceKey<Block> key(Block block) {
        return block.builtInRegistryHolder().key();
    }
}