package dev.lunavisual.mixin.client;

import dev.lunavisual.client.LunaVisualConfig;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.fog.DarknessEffectFogModifier;
import net.minecraft.client.render.fog.StatusEffectFogModifier;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectFogModifier.class)
abstract class DarknessFogModifierMixin {
    @Inject(method = "shouldApply", at = @At("HEAD"), cancellable = true)
    private void lunavisual$hideOnlyDarknessFog(@Nullable CameraSubmersionType submersionType,
                                             Entity cameraEntity,
                                             CallbackInfoReturnable<Boolean> callback) {
        if (LunaVisualConfig.wardenDarkness && (Object) this instanceof DarknessEffectFogModifier) {
            callback.setReturnValue(false);
        }
    }
}
