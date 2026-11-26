package com.allen6297.ecologica.registry;

import com.allen6297.ecologica.Ecologica;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EcologicaItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Ecologica.MOD_ID);

    public static final DeferredItem<BlockItem> BOG_IRON_ORE = ITEMS.registerSimpleBlockItem(EcologicaBlocks.BOG_IRON_ORE);
    public static final DeferredItem<Item> RAW_BOG_IRON = ITEMS.registerSimpleItem("raw_bog_iron");

    private EcologicaItems() {}
}
