package com.allen6297.ecologica.worldgen;

import java.util.List;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.tag.EcologicaTags;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.OffsetPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

/**
 * Worldgen for the fen. 26.3 registers a {@link Feature} directly; placement is still a separate
 * {@link PlacedFeature}. Biome modifiers point at the placed features.
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

    public static final ResourceKey<Feature> BOG_IRON_ORE = feature("bog_iron_ore");
    /** Must match {@code features} in {@code data/ecologica/neoforge/biome_modifier/bog_iron_ore.json}. */
    public static final ResourceKey<PlacedFeature> BOG_IRON_ORE_PLACED = placed("bog_iron_ore");

    public static final ResourceKey<Feature> FEN_MOSS = feature("fen_moss");
    /** Must match {@code features} in {@code data/ecologica/neoforge/biome_modifier/fen_moss.json}. */
    public static final ResourceKey<PlacedFeature> FEN_MOSS_PLACED = placed("fen_moss");

    public static final ResourceKey<Feature> PEAT = feature("peat");
    /** Must match {@code features} in {@code data/ecologica/neoforge/biome_modifier/peat.json}. */
    public static final ResourceKey<PlacedFeature> PEAT_PLACED = placed("peat");

    public static void bootstrapFeatures(BootstrapContext<Feature> context) {
        RuleTest wetlandSediment = new TagMatchTest(EcologicaTags.Blocks.BOG_IRON_REPLACEABLES);
        context.register(
                BOG_IRON_ORE,
                new OreFeature(wetlandSediment, EcologicaBlocks.BOG_IRON_ORE.get().defaultBlockState(), BOG_IRON_VEIN_SIZE));

        // Peat replaces mineral sediment only, so a vein does not rewrite peat that is already there.
        context.register(
                PEAT,
                new OreFeature(
                        List.of(
                                new BlockReplacement(new BlockMatchTest(Blocks.MUD), EcologicaBlocks.PEAT.get().defaultBlockState()),
                                new BlockReplacement(new BlockMatchTest(Blocks.DIRT), EcologicaBlocks.PEAT.get().defaultBlockState()),
                                new BlockReplacement(new BlockMatchTest(Blocks.CLAY), EcologicaBlocks.PEAT.get().defaultBlockState())),
                        PEAT_VEIN_SIZE));

        // Moss only survives on wetland sediment, so attempts on grass or water fail.
        context.register(FEN_MOSS, new SimpleBlockFeature(BlockStateProvider.of(EcologicaBlocks.FEN_MOSS.get())));
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<Feature> features = context.lookup(Registries.FEATURE);
        context.register(
                BOG_IRON_ORE_PLACED,
                new PlacedFeature(
                        features.getOrThrow(BOG_IRON_ORE),
                        List.of(
                                CountPlacement.of(BOG_IRON_VEINS_PER_CHUNK),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(BOG_IRON_MIN_Y),
                                        VerticalAnchor.absolute(BOG_IRON_MAX_Y)),
                                BiomeFilter.biome())));
        context.register(
                PEAT_PLACED,
                new PlacedFeature(
                        features.getOrThrow(PEAT),
                        List.of(
                                CountPlacement.of(PEAT_VEINS_PER_CHUNK),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(PEAT_MIN_Y),
                                        VerticalAnchor.absolute(PEAT_MAX_Y)),
                                BiomeFilter.biome())));
        context.register(
                FEN_MOSS_PLACED,
                new PlacedFeature(
                        features.getOrThrow(FEN_MOSS),
                        List.of(
                                CountPlacement.of(FEN_MOSS_PATCHES_PER_CHUNK),
                                InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome(),
                                CountPlacement.of(FEN_MOSS_TRIES),
                                OffsetPlacement.ofTriangle(FEN_MOSS_XZ_SPREAD, FEN_MOSS_Y_SPREAD))));
    }

    private static ResourceKey<Feature> feature(String path) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, path));
    }

    private static ResourceKey<PlacedFeature> placed(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, path));
    }

    private EcologicaWorldgen() {}
}
