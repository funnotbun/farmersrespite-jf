package com.chefsdelights.farmersrespite.core.registry;

import com.chefsdelights.farmersrespite.common.item.DrinkableItem;
import com.chefsdelights.farmersrespite.common.item.KettleBlockItem;
import com.chefsdelights.farmersrespite.common.item.PurulentTeaItem;
import com.chefsdelights.farmersrespite.common.item.RoseHipTeaItem;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.utility.FRFoods;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class FRItems {

    public static List<Item> ITEMS = new ArrayList<>();

    // Items
    public static final Item KETTLE = register("kettle", new KettleBlockItem(FRBlocks.KETTLE, props("kettle").stacksTo(1)));

    public static final Item WILD_TEA_BUSH = register("wild_tea_bush", new BlockItem(FRBlocks.WILD_TEA_BUSH, props("wild_tea_bush")));

    public static final Item TEA_SEEDS = register("tea_seeds", new BlockItem(FRBlocks.SMALL_TEA_BUSH, props("tea_seeds")));
    public static final Item COFFEE_BEANS = register("coffee_beans", new BlockItem(FRBlocks.COFFEE_BUSH, props("coffee_beans")));

    public static final Item GREEN_TEA_LEAVES = register("green_tea_leaves", new Item(props("green_tea_leaves")));
    public static final Item YELLOW_TEA_LEAVES = register("yellow_tea_leaves", new Item(props("yellow_tea_leaves")));
    public static final Item BLACK_TEA_LEAVES = register("black_tea_leaves", new Item(props("black_tea_leaves")));
    public static final Item COFFEE_BERRIES = register("coffee_berries", new Item(FRFoods.COFFEE_BERRIES.apply(props("coffee_berries"))));
    public static final Item ROSE_HIPS = register("rose_hips", new Item(props("rose_hips")));

    public static final Item GREEN_TEA = register("green_tea", new DrinkableItem(FRFoods.GREEN_TEA.apply(props("green_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item YELLOW_TEA = register("yellow_tea", new DrinkableItem(FRFoods.YELLOW_TEA.apply(props("yellow_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item BLACK_TEA = register("black_tea", new DrinkableItem(FRFoods.BLACK_TEA.apply(props("black_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item ROSE_HIP_TEA = register("rose_hip_tea", new RoseHipTeaItem(2, props("rose_hip_tea").craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final Item DANDELION_TEA = register("dandelion_tea", new DrinkableItem(FRFoods.DANDELION_TEA.apply(props("dandelion_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item PURULENT_TEA = register("purulent_tea", new PurulentTeaItem(300, FRFoods.PURULENT_TEA.apply(props("purulent_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final Item COFFEE = register("coffee", new DrinkableItem(FRFoods.COFFEE.apply(props("coffee")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));

    public static final Item LONG_GREEN_TEA = register("long_green_tea", new DrinkableItem(FRFoods.LONG_GREEN_TEA.apply(props("long_green_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item LONG_YELLOW_TEA = register("long_yellow_tea", new DrinkableItem(FRFoods.LONG_YELLOW_TEA.apply(props("long_yellow_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item LONG_BLACK_TEA = register("long_black_tea", new DrinkableItem(FRFoods.LONG_BLACK_TEA.apply(props("long_black_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item LONG_DANDELION_TEA = register("long_dandelion_tea", new DrinkableItem(FRFoods.LONG_DANDELION_TEA.apply(props("long_dandelion_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item LONG_COFFEE = register("long_coffee", new DrinkableItem(FRFoods.LONG_COFFEE.apply(props("long_coffee")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item LONG_APPLE_CIDER = register("long_apple_cider", new DrinkableItem(FRFoods.LONG_APPLE_CIDER.apply(props("long_apple_cider")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));

    public static final Item STRONG_GREEN_TEA = register("strong_green_tea", new DrinkableItem(FRFoods.STRONG_GREEN_TEA.apply(props("strong_green_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item STRONG_YELLOW_TEA = register("strong_yellow_tea", new DrinkableItem(FRFoods.STRONG_YELLOW_TEA.apply(props("strong_yellow_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item STRONG_BLACK_TEA = register("strong_black_tea", new DrinkableItem(FRFoods.STRONG_BLACK_TEA.apply(props("strong_black_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item STRONG_PURULENT_TEA = register("strong_purulent_tea", new PurulentTeaItem(600, FRFoods.STRONG_PURULENT_TEA.apply(props("strong_purulent_tea")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final Item STRONG_ROSE_HIP_TEA = register("strong_rose_hip_tea", new RoseHipTeaItem(4, props("strong_rose_hip_tea").craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final Item STRONG_COFFEE = register("strong_coffee", new DrinkableItem(FRFoods.STRONG_COFFEE.apply(props("strong_coffee")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));
    public static final Item STRONG_MELON_JUICE = register("strong_melon_juice", new RoseHipTeaItem(4, props("strong_melon_juice").craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final Item STRONG_APPLE_CIDER = register("strong_apple_cider", new DrinkableItem(FRFoods.STRONG_APPLE_CIDER.apply(props("strong_apple_cider")).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, false));

    public static final Item GREEN_TEA_COOKIE = register("green_tea_cookie", new Item(FRFoods.GREEN_TEA_COOKIE.apply(props("green_tea_cookie"))));
    public static final Item NETHER_WART_SOURDOUGH = register("nether_wart_sourdough", new Item(FRFoods.NETHER_WART_SOURDOUGH.apply(props("nether_wart_sourdough"))));

    public static final Item BLACK_COD = register("black_cod", new ConsumableItem(FRFoods.BLACK_COD.apply(props("black_cod")).craftRemainder(Items.BOWL).stacksTo(16), true));
    public static final Item TEA_CURRY = register("tea_curry", new ConsumableItem(FRFoods.TEA_CURRY.apply(props("tea_curry")).craftRemainder(Items.BOWL).stacksTo(16), true));
    public static final Item BLAZING_CHILI = register("blazing_chili", new ConsumableItem(FRFoods.BLAZING_CHILLI.apply(props("blazing_chili")).craftRemainder(Items.BOWL).stacksTo(16), true));

    public static final Item COFFEE_CAKE = register("coffee_cake", new BlockItem(FRBlocks.COFFEE_CAKE, props("coffee_cake").stacksTo(1)));
    public static final Item COFFEE_CAKE_SLICE = register("coffee_cake_slice", new Item(FRFoods.COFFEE_CAKE_SLICE.apply(props("coffee_cake_slice"))));
    public static final Item ROSE_HIP_PIE = register("rose_hip_pie", new BlockItem(FRBlocks.ROSE_HIP_PIE, props("rose_hip_pie")));
    public static final Item ROSE_HIP_PIE_SLICE = register("rose_hip_pie_slice", new Item(FRFoods.ROSE_HIP_PIE_SLICE.apply(props("rose_hip_pie_slice"))));

    public static Item.Properties props(String path) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, FarmersRespite.id(path)));
    }

    public static <T extends Item> T register(String path, T item) {
        ITEMS.add(item);
        return Registry.register(BuiltInRegistries.ITEM, FarmersRespite.id(path), item);
    }
}
