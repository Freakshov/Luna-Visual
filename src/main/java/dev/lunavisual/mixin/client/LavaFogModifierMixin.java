package dev.lunavisual.mixin.client;

import dev.lunavisual.client.LavaVisibility;
import dev.lunavisual.client.FogVisibility;
import dev.lunavisual.client.LunaVisualConfig;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.LavaFogModifier;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFogModifier.class)
abstract class LavaFogModifierMixin {
    @Inject(method = "applyStartEndModifier", at = @At("TAIL"))
    private void lunavisual$clearLavaFog(FogData data, Camera camera, ClientWorld world,
                                       float viewDistance, RenderTickCounter tickCounter,
                                       CallbackInfo callback) {
        if (LunaVisualConfig.clearFluids || (LunaVisualConfig.lavaClearView && LavaVisibility.shouldReduceLavaFog())) {
            FogVisibility.clear(data);
        }
    }
}
