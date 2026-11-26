package com.allen6297.ecologica.datagen;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class EcologicaBlockStateProvider extends BlockStateProvider {
    public EcologicaBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Ecologica.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(EcologicaBlocks.BOG_IRON_ORE.get(), cubeAll(EcologicaBlocks.BOG_IRON_ORE.get()));
        var fenMoss = models().withExistingParent("fen_moss", "minecraft:block/carpet")
                .texture("wool", modLoc("block/fen_moss"));
        simpleBlockWithItem(EcologicaBlocks.FEN_MOSS.get(), fenMoss);
    }
}
