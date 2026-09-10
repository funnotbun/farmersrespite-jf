package com.chefsdelights.farmersrespite.core.registry;

import com.chefsdelights.farmersrespite.common.block.*;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import vectorwing.farmersdelight.common.block.PieBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;

import java.util.function.ToIntFunction;

@SuppressWarnings("unused")
public class FRBlocks {

    //Blocks

    // Workstations
    public static final Block KETTLE = register("kettle", new KettleBlock(props("kettle").mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));

    //Tea
    public static final Block TEA_BUSH = register("tea_bush", new TeaBushBlock(props("tea_bush").instabreak().sound(SoundType.GRASS).noOcclusion()));
    public static final Block SMALL_TEA_BUSH = register("small_tea_bush", new SmallTeaBushBlock(props("small_tea_bush").instabreak().sound(SoundType.GRASS).noOcclusion()));
    public static final Block WILD_TEA_BUSH = register("wild_tea_bush", new WildTeaBushBlock(props("wild_tea_bush").instabreak().sound(SoundType.GRASS).noOcclusion()));

    //Coffee
    public static final Block COFFEE_BUSH = register("coffee_bush", new CoffeeBushBlock(props("coffee_bush").instabreak().sound(SoundType.GRASS).noOcclusion()));
    public static final Block COFFEE_STEM = register("coffee_stem", new CoffeeStemBlock(props("coffee_stem").instabreak().sound(SoundType.GRASS).noOcclusion()));
    public static final Block COFFEE_BUSH_TOP = register("coffee_bush_top", new CoffeeBushTopBlock(props("coffee_bush_top").instabreak().sound(SoundType.GRASS).noOcclusion()));
    public static final Block COFFEE_STEM_DOUBLE = register("coffee_stem_double", new CoffeeDoubleStemBlock(props("coffee_stem_double").instabreak().sound(SoundType.GRASS).noOcclusion()));
    public static final Block COFFEE_STEM_MIDDLE = register("coffee_stem_middle", new CoffeeMiddleStemBlock(props("coffee_stem_middle").instabreak().sound(SoundType.GRASS).noOcclusion()));
    public static final Block WITHER_ROOTS = register("wither_roots", new WitherRootsBlock(props("wither_roots").replaceable().noCollision().instabreak().sound(SoundType.GRASS)));
    public static final Block WITHER_ROOTS_PLANT = register("wither_roots_plant", new WitherRootsBlock(props("wither_roots_plant").replaceable().noCollision().instabreak().sound(SoundType.GRASS)));

    //Food
    public static final Block COFFEE_CAKE = register("coffee_cake", new CoffeeCakeBlock(props("coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL)));
    public static final Block ROSE_HIP_PIE = register("rose_hip_pie", new PieBlock(props("rose_hip_pie").mapColor(MapColor.PLANT).strength(0.5F).sound(SoundType.WOOL), () -> FRItems.ROSE_HIP_PIE_SLICE));
    public static final Block CANDLE_COFFEE_CAKE = register("candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.CANDLE, props("candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block WHITE_CANDLE_COFFEE_CAKE = register("white_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.white(), props("white_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block ORANGE_CANDLE_COFFEE_CAKE = register("orange_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.orange(), props("orange_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block MAGENTA_CANDLE_COFFEE_CAKE = register("magenta_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.magenta(), props("magenta_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block LIGHT_BLUE_CANDLE_COFFEE_CAKE = register("light_blue_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.lightBlue(), props("light_blue_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block YELLOW_CANDLE_COFFEE_CAKE = register("yellow_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.yellow(), props("yellow_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block LIME_CANDLE_COFFEE_CAKE = register("lime_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.lime(), props("lime_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block PINK_CANDLE_COFFEE_CAKE = register("pink_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.pink(), props("pink_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block GRAY_CANDLE_COFFEE_CAKE = register("gray_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.gray(), props("gray_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block LIGHT_GRAY_CANDLE_COFFEE_CAKE = register("light_gray_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.lightGray(), props("light_gray_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block CYAN_CANDLE_COFFEE_CAKE = register("cyan_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.cyan(), props("cyan_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block PURPLE_CANDLE_COFFEE_CAKE = register("purple_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.purple(), props("purple_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block BLUE_CANDLE_COFFEE_CAKE = register("blue_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.blue(), props("blue_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block BROWN_CANDLE_COFFEE_CAKE = register("brown_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.brown(), props("brown_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block GREEN_CANDLE_COFFEE_CAKE = register("green_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.green(), props("green_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block RED_CANDLE_COFFEE_CAKE = register("red_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.red(), props("red_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));
    public static final Block BLACK_CANDLE_COFFEE_CAKE = register("black_candle_coffee_cake", new CoffeeCandleCakeBlock(Blocks.DYED_CANDLE.black(), props("black_candle_coffee_cake").mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL).lightLevel(litBlockEmission(3))));

    //Decoration
    public static final Block POTTED_TEA_BUSH = register("potted_tea_bush", new FlowerPotBlock(FRBlocks.SMALL_TEA_BUSH, props("potted_tea_bush").instabreak().noOcclusion()));
    public static final Block POTTED_WILD_TEA_BUSH = register("potted_wild_tea_bush", new FlowerPotBlock(FRBlocks.WILD_TEA_BUSH, props("potted_wild_tea_bush").instabreak().noOcclusion()));
    public static final Block POTTED_COFFEE_BUSH = register("potted_coffee_bush", new FlowerPotBlock(FRBlocks.COFFEE_BUSH, props("potted_coffee_bush").instabreak().noOcclusion()));

    public static BlockBehaviour.Properties props(String path) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, FarmersRespite.id(path)));
    }

    public static <T extends Block> T register(String path, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, FarmersRespite.id(path), block);
    }

    private static ToIntFunction<BlockState> litBlockEmission(int level) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? level : 0;
    }
}
