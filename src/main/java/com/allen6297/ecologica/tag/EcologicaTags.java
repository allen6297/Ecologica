package com.allen6297.ecologica.tag;

import com.allen6297.ecologica.Ecologica;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class EcologicaTags {
    public static final class Blocks {
        /** Wetland sediments bog iron is allowed to replace. Packs can add their own muds and soils. */
        public static final TagKey<Block> BOG_IRON_REPLACEABLES = TagKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "bog_iron_replaceables"));

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
                ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "has_bog_iron"));

        private Biomes() {}
    }

    private EcologicaTags() {}
}
