package com.chefsdelights.farmersrespite.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.Collection;
import java.util.function.Consumer;

public class DrinkableItem extends ConsumableItem {
    public DrinkableItem(Item.Properties settings) {
        super(settings);
    }

    public DrinkableItem(Properties properties, boolean hasFoodEffectTooltip) {
        super(properties, hasFoodEffectTooltip);
    }

    public DrinkableItem(Properties properties, boolean hasPotionEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasPotionEffectTooltip, hasCustomTooltip);
    }

    public void affectConsumer(ItemStack stack, Level world, LivingEntity user) {
        Collection<Holder<MobEffect>> activeStatusEffectList = user.getActiveEffectsMap().keySet();
        if (!activeStatusEffectList.isEmpty()) {
            activeStatusEffectList.stream().skip(world.getRandom().nextInt(activeStatusEffectList.size())).findFirst().ifPresent(user::removeEffect);
        }

    }

    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 32;
    }

    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        user.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag context2) {
        super.appendHoverText(stack, context, display, tooltip, context2);
        MutableComponent empty = Component.translatable("tooltip.farmersdelight.milk_bottle");
        tooltip.accept(empty.withStyle(ChatFormatting.BLUE));
    }
}
