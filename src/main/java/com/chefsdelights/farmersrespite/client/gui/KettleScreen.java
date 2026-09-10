package com.chefsdelights.farmersrespite.client.gui;

import com.chefsdelights.farmersrespite.common.block.entity.container.KettleContainer;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.utility.FRTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KettleScreen extends AbstractRecipeBookScreen<KettleContainer> {
    private static final Identifier BACKGROUND_TEXTURE = FarmersRespite.id("textures/gui/kettle.png");
    private static final Rectangle HEAT_ICON = new Rectangle(41, 55, 17, 15);
    private static final Rectangle PROGRESS_ARROW = new Rectangle(62, 25, 0, 17);
    private static final Rectangle WATER_BAR1 = new Rectangle(34, 38, 5, 11);
    private static final Rectangle WATER_BAR2 = new Rectangle(34, 28, 5, 10);
    private static final Rectangle WATER_BAR3 = new Rectangle(34, 17, 5, 12);

    public KettleScreen(KettleContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, new KettleRecipeBookComponent(screenContainer), inv, titleIn);
        this.titleLabelX = 35;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 35;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 5, this.height / 2 - 49);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        if (this.menu.isHeated()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON.x, this.topPos + HEAT_ICON.y, 176, 0, HEAT_ICON.width, HEAT_ICON.height, 256, 256);
        }

        int l = this.menu.getBrewProgressionScaled();
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176, 15, l + 1, PROGRESS_ARROW.height, 256, 256);

        if (this.menu.waterLevel() == 1) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + WATER_BAR1.x, this.topPos + WATER_BAR1.y, 176, 53, WATER_BAR1.width, WATER_BAR1.height, 256, 256);
        }
        if (this.menu.waterLevel() == 2) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + WATER_BAR1.x, this.topPos + WATER_BAR1.y, 176, 53, WATER_BAR1.width, WATER_BAR1.height, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + WATER_BAR2.x, this.topPos + WATER_BAR2.y, 176, 43, WATER_BAR2.width, WATER_BAR2.height, 256, 256);
        }
        if (this.menu.waterLevel() == 3) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + WATER_BAR1.x, this.topPos + WATER_BAR1.y, 176, 53, WATER_BAR1.width, WATER_BAR1.height, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + WATER_BAR2.x, this.topPos + WATER_BAR2.y, 176, 43, WATER_BAR2.width, WATER_BAR2.height, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + WATER_BAR3.x, this.topPos + WATER_BAR3.y, 176, 32, WATER_BAR3.width, WATER_BAR3.height, 256, 256);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.renderMealDisplayTooltip(graphics, mouseX, mouseY)) {
            return;
        }
        super.extractTooltip(graphics, mouseX, mouseY);
        this.renderHeatIndicatorTooltip(graphics, mouseX, mouseY);
        this.renderWaterBarIndicatorTooltip(graphics, mouseX, mouseY);
    }

    private void renderHeatIndicatorTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.isHovering(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, mouseX, mouseY)) {
            String key = "container.kettle." + (this.menu.isHeated() ? "heated" : "not_heated");
            graphics.setTooltipForNextFrame(FRTextUtils.getTranslation(key, menu), mouseX, mouseY);
        }
    }

    private void renderWaterBarIndicatorTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.isHovering(34, 17, 5, 32, mouseX, mouseY)) {
            MutableComponent key = null;
            if (this.menu.waterLevel() == 0) {
                key = FRTextUtils.getTranslation("container.kettle.no_water");
            }
            if (this.menu.waterLevel() == 1) {
                key = FRTextUtils.getTranslation("container.kettle.has_single_water");
            }
            if (this.menu.waterLevel() > 1) {
                key = FRTextUtils.getTranslation("container.kettle.has_many_water", this.menu.waterLevel());
            }
            graphics.setTooltipForNextFrame(key, mouseX, mouseY);
        }
    }

    protected boolean renderMealDisplayTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.minecraft.player != null && this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            if (this.hoveredSlot.index == 2) {
                List<Component> tooltip = new ArrayList<>();
                ItemStack mealStack = this.hoveredSlot.getItem();
                tooltip.add(Component.translatable(mealStack.getItem().getDescriptionId()).withStyle(mealStack.getRarity().color()));
                ItemStack containerStack = this.menu.tileEntity.getMealContainer();
                String container = !containerStack.isEmpty() ? Component.translatable(containerStack.getItem().getDescriptionId()).getString() : "";
                tooltip.add(FRTextUtils.getTranslation("container.kettle.served_on", container).withStyle(ChatFormatting.GRAY));
                graphics.setTooltipForNextFrame(this.font, tooltip, Optional.empty(), mouseX, mouseY);
                return true;
            }
        }
        return false;
    }
}
