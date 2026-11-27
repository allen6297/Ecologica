package com.allen6297.ecologica.datagen;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class EcologicaItemTagsProvider extends ItemTagsProvider {
    public EcologicaItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookupProvider, Ecologica.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.RAW_MATERIALS_IRON).add(key(EcologicaItems.RAW_BOG_IRON.get()));
        tag(Tags.Items.ORES_IRON).add(key(EcologicaBlocks.BOG_IRON_ORE.get().asItem()));
        tag(Tags.Items.ORE_RATES_SINGULAR).add(key(EcologicaBlocks.BOG_IRON_ORE.get().asItem()));
    }

    private static ResourceKey<Item> key(Item item) {
        return item.builtInRegistryHolder().key();
    }
}