package com.allen6297.ecologica.worldgen;

import java.util.List;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.tag.EcologicaTags;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
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

    public static final int FEN_MOSS_TRIES = 16;
    public static final int FEN_MOSS_XZ_SPREAD = 4;
    public static final int FEN_MOSS_Y_SPREAD = 2;
    public static final int FEN_MOSS_PATCHES_PER_CHUNK = 2;

    public static final int PEAT_VEIN_SIZE = 5;
    public static final int PEAT_VEINS_PER_CHUNK = 4;
    public static final int PEAT_MIN_Y = 60;
    public static final int PEAT_MAX_Y = 72;

    public static final ResourceKey<ConfiguredFeature<?, ?>> BOG_IRON_ORE_CONFIGURED = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "bog_iron_ore"));

    /** Must match {@code features} in {@code data/ecologica/neoforge/biome_modifier/bog_iron_ore.json}. */
    public static final ResourceKey<PlacedFeature> BOG_IRON_ORE_PLACED = ResourceKey.create(
            Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "bog_iron_ore"));

    public static final ResourceKey<ConfiguredFeature<?, ?>> FEN_MOSS_CONFIGURED = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "fen_moss"));

    /** Must match {@code features} in {@code data/ecologica/neoforge/biome_modifier/fen_moss.json}. */
    public static final ResourceKey<PlacedFeature> FEN_MOSS_PLACED = ResourceKey.create(
            Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "fen_moss"));

    public static final ResourceKey<ConfiguredFeature<?, ?>> PEAT_CONFIGURED = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "peat"));

    /** Must match {@code features} in {@code data/ecologica/neoforge/biome_modifier/peat.json}. */
    public static final ResourceKey<PlacedFeature> PEAT_PLACED = ResourceKey.create(
            Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "peat"));

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

        // Moss only survives on wetland sediment, so the patch lands on mud and dirt banks and skips grass and water.
        context.register(
                FEN_MOSS_CONFIGURED,
                new ConfiguredFeature<>(
                        Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(
                                FEN_MOSS_TRIES,
                                FEN_MOSS_XZ_SPREAD,
                                FEN_MOSS_Y_SPREAD,
                                PlacementUtils.filtered(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockConfiguration(BlockStateProvider.simple(EcologicaBlocks.FEN_MOSS.get())),
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE))));

        // Peat replaces mineral wetland sediment only, so a vein does not rewrite peat that is already there.
        context.register(
                PEAT_CONFIGURED,
                new ConfiguredFeature<>(
                        Feature.ORE,
                        new OreConfiguration(
                                List.of(
                                        OreConfiguration.target(new BlockMatchTest(Blocks.MUD), EcologicaBlocks.PEAT.get().defaultBlockState()),
                                        OreConfiguration.target(new BlockMatchTest(Blocks.DIRT), EcologicaBlocks.PEAT.get().defaultBlockState()),
                                        OreConfiguration.target(new BlockMatchTest(Blocks.CLAY), EcologicaBlocks.PEAT.get().defaultBlockState())),
                                PEAT_VEIN_SIZE)));
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
        context.register(
                FEN_MOSS_PLACED,
                new PlacedFeature(
                        configured.getOrThrow(FEN_MOSS_CONFIGURED),
                        List.of(
                                CountPlacement.of(FEN_MOSS_PATCHES_PER_CHUNK),
                                InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome())));
        context.register(
                PEAT_PLACED,
                new PlacedFeature(
                        configured.getOrThrow(PEAT_CONFIGURED),
                        List.of(
                                CountPlacement.of(PEAT_VEINS_PER_CHUNK),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(PEAT_MIN_Y),
                                        VerticalAnchor.absolute(PEAT_MAX_Y)),
                                BiomeFilter.biome())));
    }

    private EcologicaWorldgen() {}
}
