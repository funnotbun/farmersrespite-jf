package com.chefsdelights.farmersrespite.core.mixin;

import com.chefsdelights.farmersrespite.core.registry.FRRecipeBookTypes;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.UnaryOperator;

@Mixin(RecipeBookSettings.class)
public class RecipeBookSettingsMixin {
    @Unique
    private RecipeBookSettings.TypeSettings farmersrespite$brewing = RecipeBookSettings.TypeSettings.DEFAULT;

    @Inject(method = "getSettings", at = @At("HEAD"), cancellable = true)
    private void farmersrespite$getBrewingSettings(RecipeBookType type, CallbackInfoReturnable<RecipeBookSettings.TypeSettings> cir) {
        if (type == FRRecipeBookTypes.BREWING) {
            cir.setReturnValue(this.farmersrespite$brewing);
        }
    }

    @Inject(method = "updateSettings", at = @At("HEAD"), cancellable = true)
    private void farmersrespite$updateBrewingSettings(RecipeBookType type, UnaryOperator<RecipeBookSettings.TypeSettings> updater, CallbackInfo ci) {
        if (type == FRRecipeBookTypes.BREWING) {
            this.farmersrespite$brewing = updater.apply(this.farmersrespite$brewing);
            ci.cancel();
        }
    }

    @Inject(method = "copy", at = @At("RETURN"))
    private void farmersrespite$copyBrewingSettings(CallbackInfoReturnable<RecipeBookSettings> cir) {
        ((RecipeBookSettingsMixin) (Object) cir.getReturnValue()).farmersrespite$brewing = this.farmersrespite$brewing;
    }

    @Inject(method = "replaceFrom", at = @At("RETURN"))
    private void farmersrespite$replaceBrewingSettings(RecipeBookSettings other, CallbackInfo ci) {
        this.farmersrespite$brewing = ((RecipeBookSettingsMixin) (Object) other).farmersrespite$brewing;
    }
}
