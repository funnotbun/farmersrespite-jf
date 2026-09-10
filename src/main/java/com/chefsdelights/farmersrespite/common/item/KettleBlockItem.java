package com.chefsdelights.farmersrespite.common.item;

import com.chefsdelights.farmersrespite.common.block.entity.KettleBlockEntity;
import com.chefsdelights.farmersrespite.core.utility.FRTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

import java.util.Optional;
import java.util.function.Consumer;

public class KettleBlockItem extends BlockItem {
    public KettleBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        TypedEntityData<BlockEntityType<?>> data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        Optional<CompoundTag> inventoryTag = data != null ? data.copyTagWithoutId().getCompound("Inventory") : Optional.empty();
        if (inventoryTag.isPresent()) {
            ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, context.registries(), inventoryTag.get());
            NonNullList<ItemStack> inventory = NonNullList.withSize(KettleBlockEntity.INVENTORY_SIZE, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(input, inventory);
            ItemStack mealStack = inventory.get(KettleBlockEntity.MEAL_DISPLAY_SLOT);
            if (!mealStack.isEmpty()) {
                MutableComponent textServingsOf = mealStack.getCount() == 1
                        ? FRTextUtils.getTranslation("tooltip.kettle.single_serving")
                        : FRTextUtils.getTranslation("tooltip.kettle.many_servings", mealStack.getCount());
                tooltip.accept(textServingsOf.withStyle(ChatFormatting.GRAY));
                MutableComponent textMealName = mealStack.getHoverName().copy();
                tooltip.accept(textMealName.withStyle(mealStack.getRarity().color()));
            }
        } else {
            MutableComponent textEmpty = FRTextUtils.getTranslation("tooltip.kettle.empty");
            tooltip.accept(textEmpty.withStyle(ChatFormatting.GRAY));
        }
    }
}
