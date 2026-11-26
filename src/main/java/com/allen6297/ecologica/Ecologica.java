package com.allen6297.ecologica;

import org.slf4j.Logger;

import com.allen6297.ecologica.datagen.EcologicaDataGenerators;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaCreativeTabs;
import com.allen6297.ecologica.registry.EcologicaItems;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Ecologica.MOD_ID)
public class Ecologica {
    public static final String MOD_ID = "ecologica";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Ecologica(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(EcologicaDataGenerators::gatherData);

        EcologicaBlocks.BLOCKS.register(modEventBus);
        EcologicaItems.ITEMS.register(modEventBus);
        EcologicaCreativeTabs.TABS.register(modEventBus);

        LOGGER.info("Ecologica {} loading", modContainer.getModInfo().getVersion());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Ecologica common setup");
    }
}
