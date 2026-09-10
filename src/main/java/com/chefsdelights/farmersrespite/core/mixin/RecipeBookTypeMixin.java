package com.chefsdelights.farmersrespite.core.mixin;

import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RecipeBookType.class)
public enum RecipeBookTypeMixin {
    FARMERSRESPITE_BREWING;

    RecipeBookTypeMixin() {
    }
}
