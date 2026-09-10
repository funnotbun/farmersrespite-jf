package com.chefsdelights.farmersrespite.core;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = FarmersRespite.MOD_ID)
public class FRConfiguration implements ConfigData {
    public boolean enableBoneMealTeaBush = true;
    public boolean enableBoneMealCoffeeBush = true;

    public static FRConfiguration get() {
        return AutoConfig.getConfigHolder(FRConfiguration.class).getConfig();
    }

    public static void register() {
        AutoConfig.register(FRConfiguration.class, GsonConfigSerializer::new);
    }
}
