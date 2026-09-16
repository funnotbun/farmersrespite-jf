package com.chefsdelights.farmersrespite.integration.jei;

import com.chefsdelights.farmersrespite.common.crafting.KettleRecipe;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.registry.FRItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

public class KettleRecipeCategory implements IRecipeCategory<RecipeHolder<KettleRecipe>> {
    public static final IRecipeType<RecipeHolder<KettleRecipe>> RECIPE_TYPE = IRecipeType.create(KettleRecipe.TYPE);

    private static final Identifier BACKGROUND_IMAGE = FarmersRespite.id("textures/gui/jei/kettle.png");
    private static final Identifier GUI_IMAGE = FarmersRespite.id("textures/gui/kettle.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawable timeIcon;
    private final IDrawable expIcon;
    private final IDrawable heatIndicator;
    private final IDrawable waterBarTop;
    private final IDrawable waterBarMiddle;
    private final IDrawable waterBarBottom;

    public KettleRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BACKGROUND_IMAGE, 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FRItems.KETTLE));
        this.arrow = helper.drawableBuilder(BACKGROUND_IMAGE, 117, 0, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.timeIcon = helper.createDrawable(BACKGROUND_IMAGE, 117, 17, 8, 11);
        this.expIcon = helper.createDrawable(BACKGROUND_IMAGE, 117, 28, 9, 9);
        this.heatIndicator = helper.createDrawable(GUI_IMAGE, 176, 0, 17, 15);
        this.waterBarTop = helper.createDrawable(GUI_IMAGE, 176, 32, 5, 12);
        this.waterBarMiddle = helper.createDrawable(GUI_IMAGE, 176, 43, 5, 10);
        this.waterBarBottom = helper.createDrawable(GUI_IMAGE, 176, 53, 5, 11);
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
        return 116;
    }

    @Override
    public int getHeight() {
        return 56;
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
            builder.addSlot(RecipeIngredientRole.INPUT, 19, 1 + index * 18).add(ingredient);
            index++;
        }
        // kettle
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 10).add(recipe.result());
        // bottle output
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 39).add(recipe.result());
        if (!recipe.getOutputContainer().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 63, 39).add(recipe.getOutputContainer());
        }
    }

    @Override
    public void draw(RecipeHolder<KettleRecipe> holder, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        int arrowOffsetX = 50;
        int arrowOffsetY = 10;
        this.background.draw(graphics);
        this.arrow.draw(graphics, arrowOffsetX, arrowOffsetY);
        this.timeIcon.draw(graphics, arrowOffsetX + 4, arrowOffsetY-7);
        if (holder.value().getExperience() > 0) {
            this.expIcon.draw(graphics, arrowOffsetX+3, arrowOffsetY+12);
        }
        this.heatIndicator.draw(graphics, 18, 39);
        if (holder.value().getNeedWater()) {
            this.waterBarTop.draw(graphics, 12, 1);
            this.waterBarMiddle.draw(graphics, 12, 13);
            this.waterBarBottom.draw(graphics, 12, 23);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<KettleRecipe> holder, IRecipeSlotsView slots, double mouseX, double mouseY) {
        KettleRecipe recipe = holder.value();
        if (mouseX >= 50 && mouseX <= 74 && mouseY >= 2 && mouseY <= 30) {
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
