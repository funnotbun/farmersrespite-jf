package com.chefsdelights.farmersrespite.core.mixin.client;

import com.chefsdelights.farmersrespite.core.registry.FRRecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SearchRecipeBookCategory.class)
public enum SearchRecipeBookCategoryMixin {
    FARMERSRESPITE_BREWING(FRRecipeBookCategories.KETTLE_DRINKS);

    SearchRecipeBookCategoryMixin(RecipeBookCategory... categories) {
    }
}
