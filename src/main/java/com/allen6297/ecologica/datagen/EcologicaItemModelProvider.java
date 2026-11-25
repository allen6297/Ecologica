package com.allen6297.ecologica.datagen;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class EcologicaItemModelProvider extends ItemModelProvider {
    public EcologicaItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Ecologica.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(EcologicaItems.RAW_BOG_IRON.get());
    }
}
