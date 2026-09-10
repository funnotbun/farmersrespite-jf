package com.chefsdelights.farmersrespite.client.gui;

import com.chefsdelights.farmersrespite.client.recipe.KettleRecipeDisplay;
import com.chefsdelights.farmersrespite.common.block.entity.container.KettleContainer;
import com.chefsdelights.farmersrespite.core.mixin.client.GhostSlotsInvoker;
import com.chefsdelights.farmersrespite.core.registry.FRItems;
import com.chefsdelights.farmersrespite.core.registry.FRRecipeBookCategories;
import com.chefsdelights.farmersrespite.core.utility.FRTextUtils;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class KettleRecipeBookComponent extends RecipeBookComponent<KettleContainer> {
    private static final Identifier FILTER_ENABLED = Identifier.withDefaultNamespace("recipe_book/filter_enabled");
    private static final Identifier FILTER_DISABLED = Identifier.withDefaultNamespace("recipe_book/filter_disabled");
    private static final Identifier FILTER_ENABLED_HIGHLIGHTED = Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted");
    private static final Identifier FILTER_DISABLED_HIGHLIGHTED = Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted");
    protected static final WidgetSprites RECIPE_BOOK_BUTTONS = new WidgetSprites(FILTER_ENABLED, FILTER_DISABLED, FILTER_ENABLED_HIGHLIGHTED, FILTER_DISABLED_HIGHLIGHTED);
    private static final List<TabInfo> TABS = List.of(
            new TabInfo(SearchRecipeBookCategory.valueOf("FARMERSRESPITE_BREWING")),
            new TabInfo(FRItems.COFFEE, FRRecipeBookCategories.KETTLE_DRINKS)
    );

    public KettleRecipeBookComponent(KettleContainer menu) {
        super(menu, TABS);
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return RECIPE_BOOK_BUTTONS;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index == 0 || slot.index == 1;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents contents) {
        collection.selectRecipes(contents, display -> display instanceof KettleRecipeDisplay);
    }

    @Override
    protected Component getRecipeFilterName() {
        return FRTextUtils.getTranslation("container.recipe_book.brewable");
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay display, ContextMap context) {
        GhostSlotsInvoker slots = (GhostSlotsInvoker) ghostSlots;
        if (display instanceof KettleRecipeDisplay kettleDisplay) {
            slots.farmersrespite$setResult(this.menu.getSlot(KettleContainer.MEAL_SLOT), context, kettleDisplay.result());
            for (int i = 0; i < kettleDisplay.ingredients().size(); ++i) {
                slots.farmersrespite$setInput(this.menu.getSlot(i), context, kettleDisplay.ingredients().get(i));
            }
            if (this.menu.getSlot(KettleContainer.CONTAINER_SLOT).getItem().isEmpty() && kettleDisplay.container().isPresent()) {
                slots.farmersrespite$setInput(this.menu.getSlot(KettleContainer.CONTAINER_SLOT), context, kettleDisplay.container().get());
            }
        }
    }
}
