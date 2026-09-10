package com.chefsdelights.farmersrespite.common.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class PurulentTeaItem extends DrinkableItem {
    private final int effectBoost;

    public PurulentTeaItem(int effectBoost, Properties properties) {
        super(properties, true, true);
        this.effectBoost = effectBoost;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level worldIn, LivingEntity consumer) {
        ArrayList<Holder<MobEffect>> compatibleEffects = new ArrayList<>();
        for (MobEffectInstance effect : consumer.getActiveEffects()) {
            if (effect.getEffect().value().isBeneficial()) {
                compatibleEffects.add(effect.getEffect());
            }
        }

        if (!compatibleEffects.isEmpty()) {
            MobEffectInstance selectedEffect = consumer.getEffect(compatibleEffects.get(worldIn.getRandom().nextInt(compatibleEffects.size())));
            if (selectedEffect != null) {
                consumer.addEffect(new MobEffectInstance(selectedEffect.getEffect(), selectedEffect.getDuration() + effectBoost, selectedEffect.getAmplifier(), selectedEffect.isAmbient(), selectedEffect.isVisible(), selectedEffect.showIcon()));
            }
        }
    }
}
