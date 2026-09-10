package com.chefsdelights.farmersrespite.core;

import com.chefsdelights.farmersrespite.client.recipe.KettleRecipeDisplay;
import com.chefsdelights.farmersrespite.core.event.FRCommonSetup;
import com.chefsdelights.farmersrespite.core.event.FRGeneration;
import com.chefsdelights.farmersrespite.core.registry.*;
import com.google.common.reflect.Reflection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FarmersRespite implements ModInitializer {
    public static final String MOD_ID = "farmersrespite";
    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "group"));

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    public static final Gson FD_GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB, FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(FRBlocks.KETTLE))
                .title(Component.translatable(MOD_ID + ".group.main"))
                .build());

        Reflection.initialize(
                FRItems.class,
                FRBlocks.class,
                FRRecipeSerializers.class,
                FREffects.class,
                FRBlockEntityTypes.class,
                FRContainerTypes.class,
                FRSounds.class,
                FRBiomeFeatures.class,
                KettleRecipeDisplay.class,
                FRRecipeBookCategories.class
        );

        //registerVillagerTradeOffer();
        FRConfiguration.register();
        FRCommonSetup.init();
        FRBiomeFeatures.FRConfiguredFeaturesRegistry.registerAll();
        FRBiomeFeatures.registerAll();
        FRGeneration.init();

        CreativeModeTabEvents.modifyOutputEvent(CREATIVE_TAB).register((entries) -> {
            for (Item item : FRItems.ITEMS) {
                entries.accept(item.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        });

    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static MutableComponent i18n(String key, Object... args) {
        return Component.translatable(MOD_ID + "." + key, args);
    }

    protected void registerVillagerTradeOffer() {
//		if (FarmersDelightMod.CONFIG.isFarmersBuyFDCrops()) {
//			TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1,
//					factories -> new MerchantOffer(new ItemStack(FRItems., 26)));
//		}
    }
}
