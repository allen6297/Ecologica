package com.allen6297.ecologica.registry;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.worldgen.GreenstoneBeltFeature;
import com.allen6297.ecologica.worldgen.SproutedLichenFeature;
import com.allen6297.ecologica.worldgen.TundraColumnFeature;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EcologicaFeatures {
    public static final DeferredRegister<MapCodec<? extends Feature>> TYPES =
            DeferredRegister.create(Registries.FEATURE_TYPE, Ecologica.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<TundraColumnFeature>> TUNDRA_COLUMN =
            TYPES.register("tundra_column", () -> TundraColumnFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<GreenstoneBeltFeature>> GREENSTONE_BELT =
            TYPES.register("greenstone_belt", () -> GreenstoneBeltFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<SproutedLichenFeature>> SPROUTED_LICHEN =
            TYPES.register("sprouted_lichen", () -> SproutedLichenFeature.CODEC);

    private EcologicaFeatures() {}
}
