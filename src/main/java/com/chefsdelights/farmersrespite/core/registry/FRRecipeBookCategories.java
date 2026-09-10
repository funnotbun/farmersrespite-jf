package com.chefsdelights.farmersrespite.core.registry;

import com.chefsdelights.farmersrespite.core.FarmersRespite;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public class FRRecipeBookCategories {
    public static final RecipeBookCategory KETTLE_DRINKS = register("kettle_drinks");

    private static RecipeBookCategory register(String path) {
        return Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, FarmersRespite.id(path), new RecipeBookCategory());
    }
}
