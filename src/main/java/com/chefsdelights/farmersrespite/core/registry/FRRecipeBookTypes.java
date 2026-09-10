package com.chefsdelights.farmersrespite.core.registry;

import net.minecraft.world.inventory.RecipeBookType;

public class FRRecipeBookTypes {
    public static RecipeBookType BREWING;

    static {
        BREWING = RecipeBookType.valueOf("FARMERSRESPITE_BREWING");
    }
}
