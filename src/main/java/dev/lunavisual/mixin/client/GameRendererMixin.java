package dev.lunavisual.mixin.client;

import dev.lunavisual.client.LunaVisualConfig;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {
    @Inject(method = "getSkyDarkness", at = @At("RETURN"), cancellable = true)
    private void lunavisual$removeWardenSkyDarkness(float tickProgress,
                                                  CallbackInfoReturnable<Float> callback) {
        if (LunaVisualConfig.wardenDarkness) {
            callback.setReturnValue(0.0F);
        }
    }
}
