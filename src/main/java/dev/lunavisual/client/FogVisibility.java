package dev.lunavisual.client;

import net.minecraft.client.render.fog.FogData;

public final class FogVisibility {
    private FogVisibility() {
    }

    public static void clear(FogData data) {
        data.environmentalStart = 0.0F;
        data.environmentalEnd = 4096.0F;
        data.renderDistanceStart = 0.0F;
        data.renderDistanceEnd = 4096.0F;
    }
}
