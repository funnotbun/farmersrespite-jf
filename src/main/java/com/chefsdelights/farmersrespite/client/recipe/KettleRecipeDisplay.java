package com.chefsdelights.farmersrespite.client.recipe;

import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.Optional;

public record KettleRecipeDisplay(List<SlotDisplay> ingredients, Optional<SlotDisplay> container, SlotDisplay result, SlotDisplay craftingStation, int duration, float experience) implements RecipeDisplay {
    public static final MapCodec<KettleRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(KettleRecipeDisplay::ingredients),
            SlotDisplay.CODEC.optionalFieldOf("container").forGetter(KettleRecipeDisplay::container),
            SlotDisplay.CODEC.fieldOf("result").forGetter(KettleRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(KettleRecipeDisplay::craftingStation),
            Codec.INT.fieldOf("duration").forGetter(KettleRecipeDisplay::duration),
            Codec.FLOAT.fieldOf("experience").forGetter(KettleRecipeDisplay::experience)
    ).apply(inst, KettleRecipeDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, KettleRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), KettleRecipeDisplay::ingredients,
            ByteBufCodecs.optional(SlotDisplay.STREAM_CODEC), KettleRecipeDisplay::container,
            SlotDisplay.STREAM_CODEC, KettleRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, KettleRecipeDisplay::craftingStation,
            ByteBufCodecs.INT, KettleRecipeDisplay::duration,
            ByteBufCodecs.FLOAT, KettleRecipeDisplay::experience,
            KettleRecipeDisplay::new
    );
    public static final Type<KettleRecipeDisplay> TYPE = Registry.register(BuiltInRegistries.RECIPE_DISPLAY, FarmersRespite.id("brewing"), new Type<>(MAP_CODEC, STREAM_CODEC));

    @Override
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}
