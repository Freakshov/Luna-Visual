package dev.lunavisual.mixin.client;

import dev.lunavisual.client.LunaVisualConfig;
import dev.lunavisual.client.FogVisibility;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.WaterFogModifier;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFogModifier.class)
abstract class WaterFogModifierMixin {
    @Inject(method = "applyStartEndModifier", at = @At("TAIL"))
    private void lunavisual$clearWaterFog(FogData data, Camera camera, ClientWorld world,
                                        float viewDistance, RenderTickCounter tickCounter,
                                        CallbackInfo callback) {
        if (LunaVisualConfig.clearFluids) {
            FogVisibility.clear(data);
        }
    }
}
