package com.allen6297.ecologica.datagen;

import java.util.Set;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaItems;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * The furnace is a wasteful stand-in. The bloomery in a later milestone is the intended process;
 * this recipe exists so the first drop is already an iron source.
 */
public final class EcologicaRecipeProvider extends RecipeProvider {
    public static MultiRegistryBootstrap bootstrap() {
        return new MultiRegistryBootstrap() {
            @Override
            public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(BootstrapGetter registries) {
                new EcologicaRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
            }
        };
    }

    private EcologicaRecipeProvider(BootstrapContext<Recipe<?>> recipes, BootstrapContext<net.minecraft.advancements.Advancement> advancements) {
        super(recipes, advancements);
    }

    @Override
    protected void buildRecipes() {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(EcologicaItems.RAW_BOG_IRON.get()),
                        RecipeCategory.MISC,
                        CookingBookCategory.MISC,
                        Items.IRON_INGOT,
                        0.7F,
                        200)
                .unlockedBy("has_raw_bog_iron", has(EcologicaItems.RAW_BOG_IRON.get()))
                .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "raw_bog_iron_from_smelting")));

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(EcologicaItems.RAW_BOG_IRON.get()),
                        RecipeCategory.MISC,
                        CookingBookCategory.MISC,
                        Items.IRON_INGOT,
                        0.7F,
                        100)
                .unlockedBy("has_raw_bog_iron", has(EcologicaItems.RAW_BOG_IRON.get()))
                .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "raw_bog_iron_from_blasting")));
    }
}
