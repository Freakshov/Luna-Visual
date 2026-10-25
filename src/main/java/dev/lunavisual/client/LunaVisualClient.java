package dev.lunavisual.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class LunaVisualClient implements ClientModInitializer {
    private static KeyBinding openSettingsKey;

    public static KeyBinding getOpenSettingsKey() {
        return openSettingsKey;
    }

    @Override
    public void onInitializeClient() {
        LunaVisualConfig.load();

        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lunavisual.open_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KeyBinding.Category.create(Identifier.of("lunavisual", "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettingsKey.wasPressed()) {
                if (!(client.currentScreen instanceof LunaVisualScreen)) {
                    client.setScreen(new LunaVisualScreen(client.currentScreen));
                }
            }
        });
    }
}
