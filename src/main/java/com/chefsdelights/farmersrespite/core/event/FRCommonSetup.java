package com.chefsdelights.farmersrespite.core.event;

import com.chefsdelights.farmersrespite.common.loot.function.FRCopyMealFunction;
import com.chefsdelights.farmersrespite.core.registry.FRItems;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

public class FRCommonSetup {
    public static void init() {
        registerCompostables();
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, FRCopyMealFunction.ID, FRCopyMealFunction.CODEC);
    }

    public static void registerCompostables() {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            compostable(context, FRItems.GREEN_TEA_LEAVES, 30);
            compostable(context, FRItems.YELLOW_TEA_LEAVES, 20);
            compostable(context, FRItems.BLACK_TEA_LEAVES, 10);
            compostable(context, FRItems.COFFEE_BERRIES, 30);
            compostable(context, FRItems.TEA_SEEDS, 30);
            compostable(context, FRItems.ROSE_HIPS, 30);

            compostable(context, FRItems.GREEN_TEA_COOKIE, 85);
            compostable(context, FRItems.WILD_TEA_BUSH, 65);
            compostable(context, FRItems.COFFEE_CAKE, 100);
            compostable(context, FRItems.ROSE_HIP_PIE, 100);
            compostable(context, FRItems.COFFEE_CAKE_SLICE, 85);
            compostable(context, FRItems.ROSE_HIP_PIE_SLICE, 85);
        });
    }

    private static void compostable(DefaultItemComponentEvents.ModifyContext context, Item item, int chance) {
        context.modify(item, builder ->
                builder.set(DataComponents.COMPOSTABLE, new Compostable(new ResolvableInt.Constant(chance))));
    }
}
