package com.allen6297.ecologica.datagen;

import java.util.concurrent.CompletableFuture;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * The furnace is a wasteful stand-in. The bloomery in a later milestone is the intended process;
 * this recipe exists so the first drop is already an iron source.
 */
public class EcologicaRecipeProvider extends RecipeProvider {
    public EcologicaRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(EcologicaItems.RAW_BOG_IRON.get()),
                        RecipeCategory.MISC,
                        Items.IRON_INGOT,
                        0.7F,
                        200)
                .unlockedBy("has_raw_bog_iron", has(EcologicaItems.RAW_BOG_IRON.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "raw_bog_iron_from_smelting"));

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(EcologicaItems.RAW_BOG_IRON.get()),
                        RecipeCategory.MISC,
                        Items.IRON_INGOT,
                        0.7F,
                        100)
                .unlockedBy("has_raw_bog_iron", has(EcologicaItems.RAW_BOG_IRON.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(Ecologica.MOD_ID, "raw_bog_iron_from_blasting"));
    }
}
