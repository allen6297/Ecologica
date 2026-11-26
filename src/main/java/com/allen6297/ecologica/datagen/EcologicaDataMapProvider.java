package com.allen6297.ecologica.datagen;

import java.util.concurrent.CompletableFuture;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.block.PeatBlock;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class EcologicaDataMapProvider extends DataMapProvider {
    public EcologicaDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "peat"), new FurnaceFuel(PeatBlock.BURN_TIME), false);
    }
}
