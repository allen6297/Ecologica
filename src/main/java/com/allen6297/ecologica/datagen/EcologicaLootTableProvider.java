package com.allen6297.ecologica.datagen;

import java.util.List;
import java.util.Set;

import com.allen6297.ecologica.registry.EcologicaBlocks;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public final class EcologicaLootTableProvider {
    public static SingleRegistryBootstrap<LootTable> bootstrap() {
        return new LootTableProvider(
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)));
    }

    private static final class BlockLoot extends BlockLootSubProvider {
        private BlockLoot(LootTableSubProvider.Context context) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, context);
        }

        @Override
        public void run() {
            // The vanilla run() insists on a table for every block in the game. Write only ours.
            accept(EcologicaBlocks.BOG_IRON_ORE.get(),
                    createOreDrop(EcologicaBlocks.BOG_IRON_ORE.get(), EcologicaItems.RAW_BOG_IRON.get()));
            accept(EcologicaBlocks.FEN_MOSS.get(), createSingleItemTable(EcologicaBlocks.FEN_MOSS.get()));
            accept(EcologicaBlocks.PEAT.get(), createSingleItemTable(EcologicaBlocks.PEAT.get()));
            accept(EcologicaBlocks.DRIED_FEN_SOIL.get(), createSingleItemTable(EcologicaBlocks.DRIED_FEN_SOIL.get()));
            Block bandedIron = EcologicaBlocks.SMOOTH_BASALT_IRON_ORE.get();
            accept(bandedIron, createSilkTouchDispatchTable(
                    bandedIron,
                    applyExplosionDecay(
                            bandedIron,
                            LootItem.lootTableItem(Items.RAW_IRON)
                                    .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(2)))
                                    .apply(ApplyBonusCount.addOreBonusCount(this.enchantments.getOrThrow(Enchantments.FORTUNE))))));
            accept(EcologicaBlocks.SPROUTED_GLOW_LICHEN.get(), createSingleItemTable(EcologicaBlocks.SPROUTED_GLOW_LICHEN.get()));
            accept(EcologicaBlocks.SHEARED_GLOW_LICHEN.get(), createSingleItemTable(EcologicaBlocks.SHEARED_GLOW_LICHEN.get()));
        }

        @Override
        protected void generate() {
        }

        private void accept(Block block, net.minecraft.world.level.storage.loot.LootTable.Builder builder) {
            this.output.accept(block.getLootTable().orElseThrow(), builder);
        }
    }
}
