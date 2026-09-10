package com.chefsdelights.farmersrespite.core.utility;

import com.chefsdelights.farmersrespite.core.registry.FREffects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FRFoods {
    public record Meal(FoodProperties food, Consumable consumable) {
        public Item.Properties apply(Item.Properties properties) {
            return properties.food(food).component(DataComponents.CONSUMABLE, consumable);
        }
    }

    private static ApplyStatusEffectsConsumeEffect effect(MobEffectInstance instance, float chance) {
        return new ApplyStatusEffectsConsumeEffect(instance, chance);
    }

    private static Meal drink(ConsumeEffect... effects) {
        Consumable.Builder builder = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK);
        for (ConsumeEffect effect : effects) {
            builder.onConsume(effect);
        }
        return new Meal(new FoodProperties.Builder().alwaysEdible().build(), builder.build());
    }

    private static Meal meal(int nutrition, float saturation, boolean fast, ConsumeEffect... effects) {
        Consumable.Builder builder = Consumable.builder();
        if (fast) {
            builder.consumeSeconds(0.8F);
        }
        for (ConsumeEffect effect : effects) {
            builder.onConsume(effect);
        }
        return new Meal(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build(), builder.build());
    }

    // Drinks (mostly for effects)
    public static final Meal GREEN_TEA = drink(effect(new MobEffectInstance(MobEffects.HASTE, 3600, 0), 1.0F));
    public static final Meal YELLOW_TEA = drink(effect(new MobEffectInstance(MobEffects.RESISTANCE, 3600, 0), 1.0F));
    public static final Meal BLACK_TEA = drink(effect(new MobEffectInstance(MobEffects.POISON, 200, 0), 1.0F), effect(new MobEffectInstance(FREffects.CAFFEINATED, 200, 0), 1.0F));
    public static final Meal DANDELION_TEA = drink(effect(new MobEffectInstance(ModEffects.COMFORT, 3600, 0), 1.0F));
    public static final Meal PURULENT_TEA = drink(effect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 0), 1.0F));
    public static final Meal COFFEE = drink(effect(new MobEffectInstance(FREffects.CAFFEINATED, 6000, 1), 1.0F));

    public static final Meal LONG_GREEN_TEA = drink(effect(new MobEffectInstance(MobEffects.HASTE, 5400, 0), 1.0F));
    public static final Meal LONG_YELLOW_TEA = drink(effect(new MobEffectInstance(MobEffects.RESISTANCE, 5400, 0), 1.0F));
    public static final Meal LONG_BLACK_TEA = drink(effect(new MobEffectInstance(MobEffects.POISON, 300, 0), 1.0F), effect(new MobEffectInstance(FREffects.CAFFEINATED, 300, 0), 1.0F));
    public static final Meal LONG_DANDELION_TEA = drink(effect(new MobEffectInstance(ModEffects.COMFORT, 5400, 0), 1.0F));
    public static final Meal LONG_COFFEE = drink(effect(new MobEffectInstance(FREffects.CAFFEINATED, 12000, 0), 1.0F));
    public static final Meal LONG_APPLE_CIDER = drink(effect(new MobEffectInstance(MobEffects.ABSORPTION, 1800, 0), 1.0F));

    public static final Meal STRONG_GREEN_TEA = drink(effect(new MobEffectInstance(MobEffects.HASTE, 1800, 1), 1.0F));
    public static final Meal STRONG_YELLOW_TEA = drink(effect(new MobEffectInstance(MobEffects.RESISTANCE, 1800, 1), 1.0F));
    public static final Meal STRONG_BLACK_TEA = drink(effect(new MobEffectInstance(MobEffects.POISON, 100, 1), 1.0F), effect(new MobEffectInstance(FREffects.CAFFEINATED, 100, 1), 1.0F));
    public static final Meal STRONG_PURULENT_TEA = drink(effect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 1), 1.0F));
    public static final Meal STRONG_COFFEE = drink(effect(new MobEffectInstance(FREffects.CAFFEINATED, 3000, 2), 1.0F));
    public static final Meal STRONG_APPLE_CIDER = drink(effect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 1), 1.0F));

    // Basic Foods
    public static final Meal ROSE_HIP_PIE_SLICE = meal(3, 0.3F, true, effect(new MobEffectInstance(MobEffects.SPEED, 300, 0), 1.0F), effect(new MobEffectInstance(MobEffects.REGENERATION, 300, 0), 1.0F));
    public static final Meal GREEN_TEA_COOKIE = meal(2, 0.1F, false, effect(new MobEffectInstance(MobEffects.HASTE, 100, 0), 1.0F));
    public static final Meal NETHER_WART_SOURDOUGH = meal(4, 0.2F, false, effect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0), 0.8F));
    public static final Meal COFFEE_CAKE_SLICE = meal(3, 0.3F, true, effect(new MobEffectInstance(MobEffects.SPEED, 400, 0), 1.0F), effect(new MobEffectInstance(FREffects.CAFFEINATED, 600, 0), 1.0F));
    public static final Meal COFFEE_BERRIES = meal(2, 0.4F, false, effect(new MobEffectInstance(MobEffects.WITHER, 100, 0), 0.8F), effect(new MobEffectInstance(FREffects.CAFFEINATED, 200, 0), 1.0F));

    // Bowl Foods
    public static final Meal BLACK_COD = meal(10, 0.9F, false, effect(new MobEffectInstance(ModEffects.NOURISHMENT, 3600, 0), 1.0F), effect(new MobEffectInstance(FREffects.CAFFEINATED, 600, 0), 1.0F));
    public static final Meal TEA_CURRY = meal(10, 0.8F, false, effect(new MobEffectInstance(MobEffects.RESISTANCE, 600, 0), 1.0F));
    public static final Meal BLAZING_CHILLI = meal(10, 0.4F, false, effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0), 1.0F));
}
