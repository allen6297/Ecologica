package com.allen6297.ecologica;

import org.slf4j.Logger;

import com.allen6297.ecologica.datagen.EcologicaDataGenerators;
import com.allen6297.ecologica.gametest.EcologicaGameTests;
import com.allen6297.ecologica.block.SproutedGlowLichenBlock;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaCreativeTabs;
import com.allen6297.ecologica.registry.EcologicaFeatures;
import com.allen6297.ecologica.registry.EcologicaItems;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

@Mod(Ecologica.MOD_ID)
public class Ecologica {
    public static final String MOD_ID = "ecologica";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Ecologica(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(GatherDataEvent.Client.class, EcologicaDataGenerators::gatherData);
        modEventBus.addListener(EcologicaGameTests::register);

        EcologicaBlocks.BLOCKS.register(modEventBus);
        EcologicaItems.ITEMS.register(modEventBus);
        EcologicaCreativeTabs.TABS.register(modEventBus);
        EcologicaFeatures.TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(SproutedGlowLichenBlock::onBonemeal);

        LOGGER.info("Ecologica {} loading", modContainer.getModInfo().getVersion());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Ecologica common setup");
    }
}
