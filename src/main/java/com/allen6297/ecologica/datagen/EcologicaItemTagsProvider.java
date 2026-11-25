package com.allen6297.ecologica.datagen;

import java.util.concurrent.CompletableFuture;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class EcologicaItemTagsProvider extends ItemTagsProvider {
    public EcologicaItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Ecologica.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.RAW_MATERIALS_IRON).add(EcologicaItems.RAW_BOG_IRON.get());
        tag(Tags.Items.ORES_IRON).add(EcologicaBlocks.BOG_IRON_ORE.get().asItem());
        tag(Tags.Items.ORE_RATES_SINGULAR).add(EcologicaBlocks.BOG_IRON_ORE.get().asItem());
    }
}
