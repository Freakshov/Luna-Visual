package dev.lunavisual.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffects;

public final class LavaVisibility {
    private LavaVisibility() {
    }

    public static boolean shouldReduceLavaFog() {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        return player != null
                && client.gameRenderer.getCamera().getSubmersionType() == CameraSubmersionType.LAVA
                && (player.isCreative() || player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE));
    }
}
