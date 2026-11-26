package com.allen6297.ecologica.datagen;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EcologicaLanguageProvider extends LanguageProvider {
    public EcologicaLanguageProvider(PackOutput output) {
        super(output, Ecologica.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(EcologicaBlocks.BOG_IRON_ORE.get(), "Bog Iron Ore");
        add(EcologicaBlocks.FEN_MOSS.get(), "Fen Moss");
        add(EcologicaItems.RAW_BOG_IRON.get(), "Raw Bog Iron");
        add("itemGroup.ecologica", "Ecologica");
    }
}
