package com.allen6297.ecologica.datagen;

import java.util.concurrent.CompletableFuture;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.tag.EcologicaTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class EcologicaBlockTagsProvider extends BlockTagsProvider {
    public EcologicaBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Ecologica.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(EcologicaBlocks.BOG_IRON_ORE.get());
        tag(Tags.Blocks.ORES_IRON).add(EcologicaBlocks.BOG_IRON_ORE.get());
        tag(Tags.Blocks.ORE_RATES_SINGULAR).add(EcologicaBlocks.BOG_IRON_ORE.get());
        tag(EcologicaTags.Blocks.BOG_IRON_REPLACEABLES).add(
                Blocks.DIRT,
                Blocks.COARSE_DIRT,
                Blocks.ROOTED_DIRT,
                Blocks.MUD,
                Blocks.CLAY);
    }
}
