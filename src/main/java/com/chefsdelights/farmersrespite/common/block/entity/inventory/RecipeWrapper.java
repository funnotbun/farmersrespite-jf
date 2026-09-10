package com.chefsdelights.farmersrespite.common.block.entity.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class RecipeWrapper implements RecipeInput {
    protected final ItemHandler inventory;

    public RecipeWrapper(ItemHandler inventory) {
        this.inventory = inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.getItem(slot);
    }

    @Override
    public int size() {
        return this.inventory.getContainerSize();
    }
}
