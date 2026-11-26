package com.allen6297.ecologica.datagen;

import java.util.concurrent.CompletableFuture;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.tag.EcologicaTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class EcologicaBiomeTagsProvider extends BiomeTagsProvider {
    public EcologicaBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Ecologica.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EcologicaTags.Biomes.HAS_BOG_IRON).add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
    }
}
