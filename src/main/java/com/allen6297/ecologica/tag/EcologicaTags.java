package com.allen6297.ecologica.tag;

import com.allen6297.ecologica.Ecologica;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class EcologicaTags {
    public static final class Blocks {
        /** Wetland sediments bog iron is allowed to replace. Packs can add their own muds and soils. */
        public static final TagKey<Block> BOG_IRON_REPLACEABLES = TagKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "bog_iron_replaceables"));

        /**
         * Blocks fen moss can sit on. Includes the sediments bog iron replaces, plus bog iron
         * itself, so a concretion forming under the moss does not tear the moss up.
         */
        public static final TagKey<Block> FEN_MOSS_SUPPORTS = TagKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "fen_moss_supports"));

        private Blocks() {}
    }

    public static final class Biomes {
        /**
         * Biomes where bog iron generates. Vanilla swamps are added by datagen.
         * A datapack (or Terralith, or a later Ecologica biome) joins the system by joining this tag.
         * The biome modifier reads the tag, so no Java change is required.
         */
        public static final TagKey<Biome> HAS_BOG_IRON = TagKey.create(
                Registries.BIOME,
                Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "has_bog_iron"));

        private Biomes() {}
    }

    private EcologicaTags() {}
}
