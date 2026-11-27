package com.allen6297.ecologica.registry;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.worldgen.EcologicaFuels;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EcologicaItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Ecologica.MOD_ID);

    public static final DeferredItem<BlockItem> BOG_IRON_ORE = ITEMS.registerSimpleBlockItem(EcologicaBlocks.BOG_IRON_ORE);
    public static final DeferredItem<BlockItem> FEN_MOSS = ITEMS.registerSimpleBlockItem(EcologicaBlocks.FEN_MOSS);
    public static final DeferredItem<BlockItem> PEAT = ITEMS.registerSimpleBlockItem(
            EcologicaBlocks.PEAT, properties -> properties.cookingFuel(EcologicaFuels.PEAT_BURN_TIME));
    public static final DeferredItem<BlockItem> DRIED_FEN_SOIL = ITEMS.registerSimpleBlockItem(EcologicaBlocks.DRIED_FEN_SOIL);
    public static final DeferredItem<Item> RAW_BOG_IRON = ITEMS.registerSimpleItem("raw_bog_iron");
    public static final DeferredItem<BlockItem> SMOOTH_BASALT_IRON_ORE = ITEMS.registerSimpleBlockItem(EcologicaBlocks.SMOOTH_BASALT_IRON_ORE);
    public static final DeferredItem<BlockItem> SPROUTED_GLOW_LICHEN = ITEMS.registerSimpleBlockItem(EcologicaBlocks.SPROUTED_GLOW_LICHEN);
    public static final DeferredItem<BlockItem> SHEARED_GLOW_LICHEN = ITEMS.registerSimpleBlockItem(EcologicaBlocks.SHEARED_GLOW_LICHEN);
    public static final DeferredItem<Item> PHOSPHORUS = ITEMS.registerSimpleItem("phosphorus");

    private EcologicaItems() {}
}
