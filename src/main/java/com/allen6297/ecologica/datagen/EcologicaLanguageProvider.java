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
        add(EcologicaBlocks.PEAT.get(), "Peat");
        add(EcologicaBlocks.DRIED_FEN_SOIL.get(), "Dried Fen Soil");
        add(EcologicaItems.RAW_BOG_IRON.get(), "Raw Bog Iron");
        add(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get(), "Smooth Basalt Iron Ore");
        add(EcologicaBlocks.SPROUTED_GLOW_LICHEN.get(), "Sprouted Glow Lichen");
        add(EcologicaBlocks.SHEARED_GLOW_LICHEN.get(), "Sheared Glow Lichen");
        add(EcologicaItems.PHOSPHORUS.get(), "Phosphorus");
        add("itemGroup.ecologica", "Ecologica");
    }
}
