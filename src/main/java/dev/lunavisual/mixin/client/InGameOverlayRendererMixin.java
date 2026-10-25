package dev.lunavisual.mixin.client;

import dev.lunavisual.client.LavaVisibility;
import dev.lunavisual.client.LunaVisualConfig;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
abstract class InGameOverlayRendererMixin {
    @Inject(method = "renderFireOverlay", at = @At("HEAD"))
    private static void lunavisual$lowerLavaFireOverlay(MatrixStack matrices,
                                                      VertexConsumerProvider vertexConsumers,
                                                      Sprite sprite,
                                                      CallbackInfo callback) {
        if (LunaVisualConfig.lavaClearView && LavaVisibility.shouldReduceLavaFog()) {
            matrices.push();
            matrices.translate(0.0, -0.18, 0.0);
        }
    }

    @Inject(method = "renderFireOverlay", at = @At("TAIL"))
    private static void lunavisual$restoreOverlayMatrix(MatrixStack matrices,
                                                      VertexConsumerProvider vertexConsumers,
                                                      Sprite sprite,
                                                      CallbackInfo callback) {
        if (LunaVisualConfig.lavaClearView && LavaVisibility.shouldReduceLavaFog()) {
            matrices.pop();
        }
    }
}
