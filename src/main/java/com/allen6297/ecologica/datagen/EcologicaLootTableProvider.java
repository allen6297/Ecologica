package com.allen6297.ecologica.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class EcologicaLootTableProvider extends LootTableProvider {
    public EcologicaLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)), registries);
    }

    private static final class BlockLoot extends BlockLootSubProvider {
        private BlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected void generate() {
            add(EcologicaBlocks.BOG_IRON_ORE.get(),
                    createOreDrop(EcologicaBlocks.BOG_IRON_ORE.get(), EcologicaItems.RAW_BOG_IRON.get()));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return EcologicaBlocks.BLOCKS.getEntries().stream().map(entry -> (Block) entry.value()).toList();
        }
    }
}
