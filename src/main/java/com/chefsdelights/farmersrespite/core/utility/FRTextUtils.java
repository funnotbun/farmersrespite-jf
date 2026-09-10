package com.chefsdelights.farmersrespite.core.utility;

import com.chefsdelights.farmersrespite.core.FarmersRespite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Util for obtaining and formatting ITextComponents for use across the mod.
 */

public class FRTextUtils {
    /**
     * Syntactic sugar for custom translation keys. Always prefixed with the mod's ID in lang files (e.g. farmersdelight.your.key.here).
     */
    public static MutableComponent getTranslation(String key, Object... args) {
        return Component.translatable(FarmersRespite.MOD_ID + "." + key, args);
    }
}
