package com.allen6297.ecologica.worldgen;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.block.PeatBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public final class EcologicaFuels {
    public static final ResourceKey<ContextIntProvider> PEAT_BURN_TIME = ResourceKey.create(
            Registries.CONTEXT_INT_PROVIDER,
            Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "peat_burn_time"));

    public static void bootstrap(BootstrapContext<ContextIntProvider> context) {
        context.register(PEAT_BURN_TIME, new ConstantValue(PeatBlock.BURN_TIME));
    }

    private EcologicaFuels() {}
}
