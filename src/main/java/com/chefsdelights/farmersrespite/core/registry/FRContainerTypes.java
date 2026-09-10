package com.chefsdelights.farmersrespite.core.registry;

import com.chefsdelights.farmersrespite.common.block.entity.container.KettleContainer;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FRContainerTypes {

    public static final MenuType<KettleContainer> KETTLE = registerContainer("kettle", KettleContainer::new);

    public static <T extends AbstractContainerMenu> MenuType<T> registerContainer(String pathName, ExtendedMenuType.ExtendedFactory<T, BlockPos> screenHandlerFactory) {
        return Registry.register(BuiltInRegistries.MENU, FarmersRespite.id(pathName), new ExtendedMenuType<>(screenHandlerFactory, BlockPos.STREAM_CODEC));
    }
}
