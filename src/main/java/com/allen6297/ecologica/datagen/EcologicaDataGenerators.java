package com.allen6297.ecologica.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.worldgen.EcologicaWorldgen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class EcologicaDataGenerators {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existing = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new EcologicaBlockStateProvider(output, existing));
        generator.addProvider(event.includeClient(), new EcologicaItemModelProvider(output, existing));
        generator.addProvider(event.includeClient(), new EcologicaLanguageProvider(output));

        EcologicaBlockTagsProvider blockTags = new EcologicaBlockTagsProvider(output, lookup, existing);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new EcologicaItemTagsProvider(output, lookup, blockTags.contentsGetter(), existing));
        generator.addProvider(event.includeServer(), new EcologicaBiomeTagsProvider(output, lookup, existing));
        generator.addProvider(event.includeServer(), new EcologicaLootTableProvider(output, lookup));
        generator.addProvider(event.includeServer(), new EcologicaRecipeProvider(output, lookup));
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                output,
                lookup,
                new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, EcologicaWorldgen::bootstrapConfigured)
                        .add(Registries.PLACED_FEATURE, EcologicaWorldgen::bootstrapPlaced),
                Set.of(Ecologica.MOD_ID)));
    }

    private EcologicaDataGenerators() {}
}
