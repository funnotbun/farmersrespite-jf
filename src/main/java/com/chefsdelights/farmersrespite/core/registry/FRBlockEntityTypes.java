package com.chefsdelights.farmersrespite.core.registry;

import com.chefsdelights.farmersrespite.common.block.entity.KettleBlockEntity;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class FRBlockEntityTypes {

    public static final BlockEntityType<KettleBlockEntity> KETTLE = register("kettle", new BlockEntityType<>(KettleBlockEntity::new, Set.of(FRBlocks.KETTLE)));

    public static <T extends BlockEntityType<?>> T register(String path, T block) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, FarmersRespite.id(path), block);
    }

}