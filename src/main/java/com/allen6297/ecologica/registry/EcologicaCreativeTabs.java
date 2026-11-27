package com.allen6297.ecologica.registry;

import com.allen6297.ecologica.Ecologica;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EcologicaCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ecologica.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ECOLOGICA = TABS.register("ecologica", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.ecologica"))
            .icon(() -> new ItemStack(EcologicaItems.RAW_BOG_IRON.get()))
            .displayItems((parameters, output) -> {
                output.accept(EcologicaItems.BOG_IRON_ORE.get());
                output.accept(EcologicaItems.RAW_BOG_IRON.get());
                output.accept(EcologicaItems.FEN_MOSS.get());
                output.accept(EcologicaItems.PEAT.get());
                output.accept(EcologicaItems.DRIED_FEN_SOIL.get());
                output.accept(EcologicaItems.SMOOTH_BASALT_IRON_ORE.get());
                output.accept(EcologicaItems.SPROUTED_GLOW_LICHEN.get());
                output.accept(EcologicaItems.SHEARED_GLOW_LICHEN.get());
                output.accept(EcologicaItems.PHOSPHORUS.get());
                output.accept(EcologicaItems.QIVIUT.get());
                output.accept(EcologicaItems.MUSKOX_SPAWN_EGG.get());
            })
            .build());

    private EcologicaCreativeTabs() {}
}
