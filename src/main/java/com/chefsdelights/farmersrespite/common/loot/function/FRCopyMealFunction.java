package com.chefsdelights.farmersrespite.common.loot.function;

import com.chefsdelights.farmersrespite.common.block.entity.KettleBlockEntity;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class FRCopyMealFunction extends LootItemConditionalFunction {
    public static final Identifier ID = FarmersRespite.id("copy_meal");
    public static final MapCodec<FRCopyMealFunction> CODEC = RecordCodecBuilder.mapCodec(inst ->
            commonFields(inst).apply(inst, FRCopyMealFunction::new));

    private FRCopyMealFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    public static Builder<?> builder() {
        return simpleBuilder(FRCopyMealFunction::new);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        BlockEntity tile = context.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (tile instanceof KettleBlockEntity kettle) {
            CompoundTag tag = kettle.writeMeal(new CompoundTag(), context.getLevel().registryAccess());
            if (!tag.isEmpty()) {
                stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.<BlockEntityType<?>>of(kettle.getType(), tag));
            }
        }
        return stack;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }
}
