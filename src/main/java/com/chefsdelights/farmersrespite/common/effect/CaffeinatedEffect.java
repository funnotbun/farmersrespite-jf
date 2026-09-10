package com.chefsdelights.farmersrespite.common.effect;

import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.registry.FREffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Set;

public class CaffeinatedEffect extends MobEffect {
    public static final Set<Holder<MobEffect>> CAFFINATED_IMMUNITIES = Set.of(MobEffects.SLOWNESS, MobEffects.MINING_FATIGUE);

    public CaffeinatedEffect() {
        super(MobEffectCategory.BENEFICIAL, 12161815);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, FarmersRespite.id("caffeinated"), 0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED, FarmersRespite.id("caffeinated_strength"), 0.5F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        CAFFINATED_IMMUNITIES.forEach(entity::removeEffect);
        if (this == FREffects.CAFFEINATED.value()) {
            if (entity instanceof ServerPlayer player) {
                StatsCounter statHandler = player.getStats();
                statHandler.increment(player, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), -(24000 * (amplifier + 1)));
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
