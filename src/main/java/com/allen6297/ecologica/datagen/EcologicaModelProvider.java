package com.allen6297.ecologica.datagen;

import java.util.Optional;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public class EcologicaModelProvider extends ModelProvider {
    private static final ModelTemplate CARPET = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath("minecraft", "block/carpet")),
            Optional.empty(),
            TextureSlot.WOOL);

    public EcologicaModelProvider(PackOutput output) {
        super(output, Ecologica.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(EcologicaBlocks.BOG_IRON_ORE.get());
        blockModels.createTrivialCube(EcologicaBlocks.PEAT.get());
        blockModels.createTrivialCube(EcologicaBlocks.DRIED_FEN_SOIL.get());
        carpet(blockModels, EcologicaBlocks.FEN_MOSS.get());
        blockModels.createTrivialCube(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get());
        carpet(blockModels, EcologicaBlocks.SPROUTED_GLOW_LICHEN.get());
        carpet(blockModels, EcologicaBlocks.SHEARED_GLOW_LICHEN.get());
        itemModels.generateFlatItem(EcologicaItems.RAW_BOG_IRON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(EcologicaItems.PHOSPHORUS.get(), ModelTemplates.FLAT_ITEM);
    }

    private static void carpet(BlockModelGenerators blockModels, Block block) {
        Identifier model = CARPET.create(
                block,
                new TextureMapping().put(TextureSlot.WOOL, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput);
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
    }
}
