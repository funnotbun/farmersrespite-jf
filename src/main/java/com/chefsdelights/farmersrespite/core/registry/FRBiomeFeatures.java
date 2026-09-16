package com.chefsdelights.farmersrespite.core.registry;

import com.chefsdelights.farmersrespite.common.levelgen.feature.CoffeeBushFeature;
import com.chefsdelights.farmersrespite.common.levelgen.feature.WildTeaBushFeature;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;


public enum FRBiomeFeatures {
    WILD_COFFEE("wild_coffee_bush", CoffeeBushFeature.CODEC),
    WILD_TEA("wild_tea_bush", WildTeaBushFeature.CODEC);

    private final String pathName;
    private final MapCodec<? extends Feature> codec;

    FRBiomeFeatures(String pathName, MapCodec<? extends Feature> codec) {
        this.pathName = pathName;
        this.codec = codec;
    }

    public static void registerAll() {
        for (FRBiomeFeatures value : values()) {
            Registry.register(BuiltInRegistries.FEATURE_TYPE, FarmersRespite.id(value.pathName), value.codec);
        }
    }

    public enum FRConfiguredFeaturesRegistry {
        PATCH_WILD_TEA_BUSH("patch_wild_tea_bush"),
        PATCH_COFFEE_BUSH("patch_wild_coffee_bush");

        private final Identifier featureIdentifier;
        private ResourceKey<Feature> configuredFeatureRegistryKey;
        private ResourceKey<PlacedFeature> featureRegistryKey;

        FRConfiguredFeaturesRegistry(String featurePathName) {
            this.featureIdentifier = FarmersRespite.id(featurePathName);
        }

        public static void registerAll() {
            for (FRConfiguredFeaturesRegistry value : values()) {
                value.configuredFeatureRegistryKey = ResourceKey.create(Registries.FEATURE, value.featureIdentifier);
                value.featureRegistryKey = ResourceKey.create(Registries.PLACED_FEATURE, value.featureIdentifier);
            }
        }

        public ResourceKey<Feature> configKey() {
            return configuredFeatureRegistryKey;
        }

        public ResourceKey<PlacedFeature> key() {
            return featureRegistryKey;
        }

        public Identifier identifier() {
            return featureIdentifier;
        }
    }
}
