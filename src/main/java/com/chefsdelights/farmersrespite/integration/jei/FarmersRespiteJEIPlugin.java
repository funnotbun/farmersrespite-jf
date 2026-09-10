package com.chefsdelights.farmersrespite.integration.jei;

import com.chefsdelights.farmersrespite.client.gui.KettleScreen;
import com.chefsdelights.farmersrespite.common.block.entity.container.KettleContainer;
import com.chefsdelights.farmersrespite.common.crafting.KettleRecipe;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.registry.FRBlocks;
import com.chefsdelights.farmersrespite.core.registry.FRContainerTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class FarmersRespiteJEIPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return FarmersRespite.id("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new KettleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        List<RecipeHolder<KettleRecipe>> brewing = new ArrayList<>(level.recipeAccess().getSynchronizedRecipes().getAllOfType(KettleRecipe.TYPE));
        registration.addRecipes(KettleRecipeCategory.RECIPE_TYPE, brewing);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(KettleRecipeCategory.RECIPE_TYPE, FRBlocks.KETTLE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(KettleScreen.class, 62, 25, 40, 17, KettleRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(KettleContainer.class, FRContainerTypes.KETTLE, KettleRecipeCategory.RECIPE_TYPE, 0, 2, 5, 36);
    }
}
