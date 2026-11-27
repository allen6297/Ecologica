package com.allen6297.ecologica.client;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaEntities;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Ecologica.MOD_ID, value = Dist.CLIENT)
public final class EcologicaClient {
    private EcologicaClient() {}

    @SubscribeEvent
    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MuskoxModel.LAYER, MuskoxModel::createBodyLayer);
        event.registerLayerDefinition(MuskoxModel.BABY_LAYER, MuskoxModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EcologicaEntities.MUSKOX.get(), MuskoxRenderer::new);
    }
}
