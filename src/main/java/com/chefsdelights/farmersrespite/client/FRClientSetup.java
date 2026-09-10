package com.chefsdelights.farmersrespite.client;

import com.chefsdelights.farmersrespite.client.gui.KettleScreen;
import com.chefsdelights.farmersrespite.core.registry.FRContainerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class FRClientSetup implements ClientModInitializer {
    public void onInitializeClient() {
        // 26.x: cutout render layers are auto-detected from block/cross/crop
        // model parents; the old BlockRenderLayerMap is gone from fabric-api.
        MenuScreens.register(FRContainerTypes.KETTLE, KettleScreen::new);
    }
}

