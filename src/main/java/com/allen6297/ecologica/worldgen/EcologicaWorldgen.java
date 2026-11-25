package com.allen6297.ecologica.worldgen;

import java.util.List;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.tag.EcologicaTags;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

/**
 * Bog iron is the first geological feature. It is an ore feature only in the technical sense:
 * the vein replaces wetland sediments, and which biomes receive it is the {@code ecologica:has_bog_iron} tag.
 */
public final class EcologicaWorldgen {
    public static final int BOG_IRON_VEIN_SIZE = 7;
    public static final int BOG_IRON_VEINS_PER_CHUNK = 8;
    public static final int BOG_IRON_MIN_Y = 48;
    public static final int BOG_IRON_MAX_Y = 68;

    public static final ResourceKey<ConfiguredFeature<?, ?>> BOG_IRON_ORE_CONFIGURED = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "bog_iron_ore"));

    /** Must match {@code features} in {@code data/ecologica/neoforge/biome_modifier/bog_iron_ore.json}. */
    public static final ResourceKey<PlacedFeature> BOG_IRON_ORE_PLACED = ResourceKey.create(
            Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "bog_iron_ore"));

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest wetlandSediment = new TagMatchTest(EcologicaTags.Blocks.BOG_IRON_REPLACEABLES);
        context.register(
                BOG_IRON_ORE_CONFIGURED,
                new ConfiguredFeature<>(
                        Feature.ORE,
                        new OreConfiguration(
                                List.of(OreConfiguration.target(
                                        wetlandSediment,
                                        EcologicaBlocks.BOG_IRON_ORE.get().defaultBlockState())),
                                BOG_IRON_VEIN_SIZE)));
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(
                BOG_IRON_ORE_PLACED,
                new PlacedFeature(
                        configured.getOrThrow(BOG_IRON_ORE_CONFIGURED),
                        List.of(
                                CountPlacement.of(BOG_IRON_VEINS_PER_CHUNK),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(BOG_IRON_MIN_Y),
                                        VerticalAnchor.absolute(BOG_IRON_MAX_Y)),
                                BiomeFilter.biome())));
    }

    private EcologicaWorldgen() {}
}
