package dev.lunavisual.mixin.client;

import dev.lunavisual.client.LunaVisualConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
abstract class LightmapTextureManagerMixin {
    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Double;floatValue()F",
                    ordinal = 1
            )
    )
    private float lunavisual$useFullbrightGamma(Double gamma) {
        return LunaVisualConfig.fullbright ? 10.0F : gamma.floatValue();
    }

    @Inject(method = "getDarkness", at = @At("RETURN"), cancellable = true)
    private void lunavisual$removeDarknessLightmap(LivingEntity entity, float factor, float tickProgress,
                                                 CallbackInfoReturnable<Float> callback) {
        if (LunaVisualConfig.wardenDarkness && entity == MinecraftClient.getInstance().player) {
            callback.setReturnValue(0.0F);
        }
    }
}
