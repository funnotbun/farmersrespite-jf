package com.chefsdelights.farmersrespite.common.crafting;

import com.chefsdelights.farmersrespite.client.recipe.KettleRecipeDisplay;
import com.chefsdelights.farmersrespite.common.block.entity.inventory.RecipeWrapper;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.registry.FRItems;
import com.chefsdelights.farmersrespite.core.registry.FRRecipeBookCategories;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KettleRecipe implements Recipe<RecipeWrapper> {
    public static final RecipeType<KettleRecipe> TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, FarmersRespite.id("brewing"), new RecipeType<>() {
    });
    public static final int INPUT_SLOTS = 2;

    private static final MapCodec<KettleRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
            Ingredient.CODEC.listOf(1, INPUT_SLOTS).fieldOf("ingredients").forGetter(recipe -> recipe.inputItems),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.resultTemplate),
            ItemStackTemplate.CODEC.optionalFieldOf("container").forGetter(recipe -> recipe.containerOverride),
            Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(recipe -> recipe.experience),
            Codec.INT.optionalFieldOf("brewingtime", 2400).forGetter(recipe -> recipe.brewTime),
            Codec.BOOL.optionalFieldOf("needWater", true).forGetter(recipe -> recipe.needWater)
    ).apply(inst, KettleRecipe::new));
    private static final StreamCodec<RegistryFriendlyByteBuf, KettleRecipe> STREAM_CODEC = StreamCodec.of(KettleRecipe::toNetwork, KettleRecipe::fromNetwork);
    public static final RecipeSerializer<KettleRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private final String group;
    private final List<Ingredient> inputItems;
    private final ItemStackTemplate resultTemplate;
    private final Optional<ItemStackTemplate> containerOverride;
    private final float experience;
    public final int brewTime;
    private final boolean needWater;

    private KettleRecipe(String group, List<Ingredient> inputItems, ItemStackTemplate result, Optional<ItemStackTemplate> container, float experience, int brewTime, boolean needWater) {
        this.group = group;
        this.inputItems = List.copyOf(inputItems);
        this.resultTemplate = result;
        this.containerOverride = container;
        this.experience = experience;
        this.brewTime = brewTime;
        this.needWater = needWater;
    }

    public List<Ingredient> input() {
        return this.inputItems;
    }

    public ItemStack result() {
        return this.resultTemplate.create();
    }

    public ItemStack getOutputContainer() {
        if (this.containerOverride.isPresent()) {
            return this.containerOverride.get().create();
        }
        ItemStackTemplate remainder = result().getCraftingRemainder();
        return remainder != null ? remainder.create() : ItemStack.EMPTY;
    }

    public float getExperience() {
        return this.experience;
    }

    public int getBrewTime() {
        return this.brewTime;
    }

    public boolean getNeedWater() {
        return this.needWater;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        List<ItemStack> inputs = new ArrayList<>();
        for (int j = 0; j < INPUT_SLOTS; ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                inputs.add(itemstack);
            }
        }
        if (inputs.size() != this.inputItems.size()) {
            return false;
        }
        boolean[] used = new boolean[inputs.size()];
        outer:
        for (Ingredient ingredient : this.inputItems) {
            for (int i = 0; i < inputs.size(); ++i) {
                if (!used[i] && ingredient.test(inputs.get(i))) {
                    used[i] = true;
                    continue outer;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(RecipeWrapper input) {
        return result();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return this.group;
    }

    @Override
    public RecipeSerializer<KettleRecipe> getSerializer() {
        return KettleRecipe.SERIALIZER;
    }

    @Override
    public RecipeType<KettleRecipe> getType() {
        return KettleRecipe.TYPE;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.inputItems);
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new KettleRecipeDisplay(
                this.inputItems.stream().map(Ingredient::display).toList(),
                this.containerOverride.map(template -> (SlotDisplay) new SlotDisplay.ItemStackSlotDisplay(template)),
                new SlotDisplay.ItemStackSlotDisplay(this.resultTemplate),
                new SlotDisplay.ItemSlotDisplay(FRItems.KETTLE),
                this.brewTime,
                this.experience));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return FRRecipeBookCategories.KETTLE_DRINKS;
    }

    private static KettleRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        String group = buf.readUtf();
        List<Ingredient> inputItems = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf);
        ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buf);
        Optional<ItemStackTemplate> container = ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC).decode(buf);
        float experience = buf.readFloat();
        int brewTime = buf.readVarInt();
        boolean needWater = buf.readBoolean();
        return new KettleRecipe(group, inputItems, result, container, experience, brewTime, needWater);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buf, KettleRecipe recipe) {
        buf.writeUtf(recipe.group);
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, recipe.inputItems);
        ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.resultTemplate);
        ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC).encode(buf, recipe.containerOverride);
        buf.writeFloat(recipe.experience);
        buf.writeVarInt(recipe.brewTime);
        buf.writeBoolean(recipe.needWater);
    }
}
