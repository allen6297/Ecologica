package com.allen6297.ecologica.datagen;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.registry.EcologicaItems;
import net.minecraft.advancements.Advancement;
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
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class EcologicaRecipeProvider extends RecipeProvider {
    EcologicaRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    public static MultiRegistryBootstrap bootstrap() {
        return new MultiRegistryBootstrap() {
            @Override
            public @NonNull Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(@NonNull BootstrapGetter registries) {
                new EcologicaRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
            }
        };
    }

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

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(
                                Items.GLOW_LICHEN,
                                EcologicaItems.SPROUTED_GLOW_LICHEN.get(),
                                EcologicaItems.SHEARED_GLOW_LICHEN.get()),
                        RecipeCategory.MISC,
                        CookingBookCategory.MISC,
                        EcologicaItems.PHOSPHORUS.get(),
                        0.1F,
                        200)
                .unlockedBy("has_glow_lichen", has(Items.GLOW_LICHEN))
                .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "phosphorus_from_smelting_lichen")));

        shapeless(RecipeCategory.MISC, Items.GLOW_INK_SAC)
                .requires(EcologicaItems.PHOSPHORUS.get())
                .unlockedBy("has_phosphorus", has(EcologicaItems.PHOSPHORUS.get()))
                .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "glow_ink_sac_from_phosphorus")));
    }
}