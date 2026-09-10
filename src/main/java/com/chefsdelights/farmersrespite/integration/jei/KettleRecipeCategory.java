package com.chefsdelights.farmersrespite.integration.jei;

import com.chefsdelights.farmersrespite.common.crafting.KettleRecipe;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.registry.FRItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

public class KettleRecipeCategory implements IRecipeCategory<RecipeHolder<KettleRecipe>> {
    public static final IRecipeType<RecipeHolder<KettleRecipe>> RECIPE_TYPE = IRecipeType.create(KettleRecipe.TYPE);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrow;

    public KettleRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(130, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FRItems.KETTLE));
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public IRecipeType<RecipeHolder<KettleRecipe>> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return FarmersRespite.i18n("jei.brewing");
    }

    @Override
    public int getWidth() {
        return 130;
    }

    @Override
    public int getHeight() {
        return 60;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<KettleRecipe> holder, IFocusGroup focuses) {
        KettleRecipe recipe = holder.value();
        int index = 0;
        for (Ingredient ingredient : recipe.input()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 15, 8 + index * 18).add(ingredient);
            index++;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 21).add(recipe.result());
        if (!recipe.getOutputContainer().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 80, 43).add(recipe.getOutputContainer());
        }
    }

    @Override
    public void draw(RecipeHolder<KettleRecipe> holder, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        this.background.draw(graphics);
        this.arrow.draw(graphics, 42, 21);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<KettleRecipe> holder, IRecipeSlotsView slots, double mouseX, double mouseY) {
        KettleRecipe recipe = holder.value();
        if (mouseX >= 40 && mouseX <= 68 && mouseY >= 19 && mouseY <= 40) {
            int brewTime = recipe.getBrewTime();
            if (brewTime > 0) {
                tooltip.add(Component.translatable("gui.jei.category.smelting.time.seconds", brewTime / 20));
            }
            float experience = recipe.getExperience();
            if (experience > 0) {
                tooltip.add(Component.translatable("gui.jei.category.smelting.experience", experience));
            }
        }
    }
}
