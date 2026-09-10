package com.chefsdelights.farmersrespite.core.registry;

import com.chefsdelights.farmersrespite.common.effect.CaffeinatedEffect;
import com.chefsdelights.farmersrespite.core.FarmersRespite;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class FREffects {
    public static final Holder<MobEffect> CAFFEINATED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, FarmersRespite.id("caffeinated"), new CaffeinatedEffect());
}
