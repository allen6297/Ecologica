package com.allen6297.ecologica.datagen;

import com.allen6297.ecologica.worldgen.EcologicaFuels;
import com.allen6297.ecologica.worldgen.EcologicaWorldgen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class EcologicaDataGenerators {
    public static void gatherData(GatherDataEvent.Client event) {
        var pack = event.getDefaultPackGenerator();
        pack.createWorldRegistryObjects(new RegistrySetBuilder()
                .add(Registries.FEATURE, EcologicaWorldgen::bootstrapFeatures)
                .add(Registries.PLACED_FEATURE, EcologicaWorldgen::bootstrapPlaced));
        pack.createReloadableRegistryObjects(new RegistrySetBuilder()
                .add(Registries.CONTEXT_INT_PROVIDER, EcologicaFuels::bootstrap)
                .add(Registries.LOOT_TABLE, EcologicaLootTableProvider.bootstrap())
                .add(EcologicaRecipeProvider.bootstrap()));

        event.createProvider(EcologicaModelProvider::new);
        event.createProvider(EcologicaLanguageProvider::new);
        event.createBlockAndItemTags(EcologicaBlockTagsProvider::new, EcologicaItemTagsProvider::new);
        event.createProvider(EcologicaBiomeTagsProvider::new);
    }

    private EcologicaDataGenerators() {}
}
