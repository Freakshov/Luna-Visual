package dev.lunavisual.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class LunaVisualScreen extends Screen {
    private static final int DESIGN_WIDTH = 1160;
    private static final int DESIGN_HEIGHT = 650;
    private static final int CARD_WIDTH = 580;
    private static final int CARD_HEIGHT = 46;
    private static final int ROW_STEP = 93;
    private static final int TOGGLE_WIDTH = 120;
    private static final int TOGGLE_HEIGHT = 27;
    private static final int DONE_WIDTH = 152;
    private static final int DONE_HEIGHT = 37;
    private static final int PANEL_WIDTH = 660;
    private static final int PANEL_HEIGHT = 596;

    private final Screen parent;
    private float uiScale = 1.0f;
    private float offsetX;
    private float offsetY;

    public LunaVisualScreen(Screen parent) {
        super(Text.translatable("screen.lunavisual.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        uiScale = Math.min((float) width / DESIGN_WIDTH, (float) height / DESIGN_HEIGHT);
        offsetX = (width - DESIGN_WIDTH * uiScale) / 2.0f;
        offsetY = (height - DESIGN_HEIGHT * uiScale) / 2.0f;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client != null && client.world == null) {
            super.renderBackground(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.fill(0, 0, width, height, 0x500B1422);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(offsetX, offsetY);
        context.getMatrices().scale(uiScale, uiScale);

        renderPanel(context);
        renderTopGradientBar(context);
        renderHeader(context);

        LunaVisualConfig.Feature[] features = LunaVisualConfig.Feature.values();
        int cardX = (DESIGN_WIDTH - CARD_WIDTH) / 2;
        for (int i = 0; i < features.length; i++) {
            LunaVisualConfig.Feature feature = features[i];
            int cardY = 151 + i * ROW_STEP;
            int pointerX = (int) ((mouseX - offsetX) / uiScale);
            int pointerY = (int) ((mouseY - offsetY) / uiScale);
            boolean rowHovered = pointerX >= cardX && pointerX <= cardX + CARD_WIDTH
                    && pointerY >= cardY && pointerY <= cardY + CARD_HEIGHT;
            int cardBg = rowHovered ? 0xFF263A54 : 0xFF20334B;
            int cardBorder = rowHovered ? 0xFF3F5B7C : 0xFF344D6A;
            fillRoundedRect(context, cardX, cardY, CARD_WIDTH, CARD_HEIGHT, 12, cardBg);
            drawRoundedOutline(context, cardX, cardY, CARD_WIDTH, CARD_HEIGHT, 12, cardBorder);

            int titleY = cardY + (CARD_HEIGHT - textRenderer.fontHeight) / 2;
            context.drawTextWithShadow(textRenderer, Text.translatable(feature.titleKey()), cardX + 24, titleY,
                    0xFFF0F4FA);

            boolean enabled = LunaVisualConfig.isEnabled(feature);
            int toggleX = cardX + CARD_WIDTH - TOGGLE_WIDTH - 20;
            int toggleY = cardY + (CARD_HEIGHT - TOGGLE_HEIGHT) / 2;
            boolean toggleHovered = pointerX >= toggleX && pointerX <= toggleX + TOGGLE_WIDTH
                    && pointerY >= toggleY && pointerY <= toggleY + TOGGLE_HEIGHT;
            int toggleBg = enabled ? (toggleHovered ? 0xFF087EAD : 0xFF07658D)
                    : (toggleHovered ? 0xFF31445D : 0xFF293B53);
            int toggleBorder = enabled ? 0xFF38BDF8 : (toggleHovered ? 0xFF496582 : 0xFF293B53);
            int toggleTextColor = enabled ? 0xFFFFFFFF : (toggleHovered ? 0xFFD5E1EF : 0xFFBAC8D9);
            Text toggleText = Text.translatable(enabled ? "screen.lunavisual.status.enabled"
                    : "screen.lunavisual.status.disabled");
            fillRoundedRect(context, toggleX, toggleY, TOGGLE_WIDTH, TOGGLE_HEIGHT, 9, toggleBg);
            drawRoundedOutline(context, toggleX, toggleY, TOGGLE_WIDTH, TOGGLE_HEIGHT, 9, toggleBorder);
            context.drawCenteredTextWithShadow(textRenderer, toggleText, toggleX + TOGGLE_WIDTH / 2,
                    toggleY + (TOGGLE_HEIGHT - textRenderer.fontHeight) / 2, toggleTextColor);

            context.drawCenteredTextWithShadow(textRenderer, Text.translatable(feature.descriptionKey()),
                    DESIGN_WIDTH / 2, cardY + CARD_HEIGHT + 8, 0xFFAAB9CC);
        }

        int doneY = 535;
        int doneX = (DESIGN_WIDTH - DONE_WIDTH) / 2;
        int pointerX = (int) ((mouseX - offsetX) / uiScale);
        int pointerY = (int) ((mouseY - offsetY) / uiScale);
        boolean doneHovered = pointerX >= doneX && pointerX <= doneX + DONE_WIDTH
                && pointerY >= doneY && pointerY <= doneY + DONE_HEIGHT;
        fillRoundedRect(context, doneX, doneY, DONE_WIDTH, DONE_HEIGHT, 11,
                doneHovered ? 0xFF2C405E : 0xFF213249);
        drawRoundedOutline(context, doneX, doneY, DONE_WIDTH, DONE_HEIGHT, 11,
                doneHovered ? 0xFF587394 : 0xFF3A526E);
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("screen.lunavisual.done"),
                DESIGN_WIDTH / 2, doneY + (DONE_HEIGHT - textRenderer.fontHeight) / 2,
                doneHovered ? 0xFFFFFFFF : 0xFFE0EAF5);

        String keyName = "O";
        if (LunaVisualClient.getOpenSettingsKey() != null) {
            keyName = LunaVisualClient.getOpenSettingsKey().getBoundKeyLocalizedText().getString().toUpperCase();
        }
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("screen.lunavisual.footer", keyName),
                DESIGN_WIDTH / 2, 589, 0xFF8498B1);
        context.getMatrices().popMatrix();
    }

    private void renderHeader(DrawContext context) {
        context.getMatrices().pushMatrix();
        context.getMatrices().scale(1.55f, 1.55f);
        context.drawCenteredTextWithShadow(textRenderer, title, (int) (DESIGN_WIDTH / 2.0f / 1.55f),
                (int) (68.0f / 1.55f), 0xFFF1F5FB);
        context.getMatrices().popMatrix();
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("screen.lunavisual.subtitle"),
                DESIGN_WIDTH / 2, 111, 0xFF9FB1C8);
    }

    private void renderPanel(DrawContext context) {
        int panelX = (DESIGN_WIDTH - PANEL_WIDTH) / 2;
        int panelY = 24;
        fillRoundedRect(context, panelX - 4, panelY + 5, PANEL_WIDTH + 8, PANEL_HEIGHT + 3, 23, 0x66000000);
        fillRoundedRect(context, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 20, 0xFF142238);
        drawRoundedOutline(context, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 20, 0xFF344C68);
    }

    private void renderTopGradientBar(DrawContext context) {
        int barWidth = 620;
        int barHeight = 12;
        int barX = (DESIGN_WIDTH - barWidth) / 2;
        int barY = 29;
        int radius = 6;
        int startR = 56;
        int startG = 176;
        int startB = 190;
        int endR = 105;
        int endG = 126;
        int endB = 207;

        for (int col = 0; col < barWidth; col++) {
            float t = (float) col / (barWidth - 1);
            int r = (int) (startR + t * (endR - startR));
            int g = (int) (startG + t * (endG - startG));
            int b = (int) (startB + t * (endB - startB));
            int color = 0xFF000000 | (r << 16) | (g << 8) | b;
            int dy = 0;
            if (col < radius) {
                int dist = radius - col;
                dy = radius - (int) Math.round(Math.sqrt(Math.max(0, radius * radius - dist * dist)));
            } else if (col >= barWidth - radius) {
                int dist = col - (barWidth - radius - 1);
                dy = radius - (int) Math.round(Math.sqrt(Math.max(0, radius * radius - dist * dist)));
            }
            if (barY + barHeight - dy > barY + dy) {
                context.fill(barX + col, barY + dy, barX + col + 1, barY + barHeight - dy, color);
            }
        }
    }

    private void fillRoundedRect(DrawContext context, int x, int y, int w, int h, int radius, int color) {
        if (radius <= 0) {
            context.fill(x, y, x + w, y + h, color);
            return;
        }
        radius = Math.min(radius, Math.min(w, h) / 2);
        context.fill(x, y + radius, x + w, y + h - radius, color);
        context.fill(x + radius, y, x + w - radius, y + radius, color);
        context.fill(x + radius, y + h - radius, x + w - radius, y + h, color);
        for (int i = 0; i < radius; i++) {
            int dy = radius - i;
            int dx = radius - (int) Math.round(Math.sqrt(Math.max(0, radius * radius - dy * dy)));
            context.fill(x + dx, y + i, x + radius, y + i + 1, color);
            context.fill(x + w - radius, y + i, x + w - dx, y + i + 1, color);
            context.fill(x + dx, y + h - i - 1, x + radius, y + h - i, color);
            context.fill(x + w - radius, y + h - i - 1, x + w - dx, y + h - i, color);
        }
    }

    private void drawRoundedOutline(DrawContext context, int x, int y, int w, int h, int radius, int color) {
        if (radius <= 0) {
            context.fill(x, y, x + w, y + 1, color);
            context.fill(x, y + h - 1, x + w, y + h, color);
            context.fill(x, y, x + 1, y + h, color);
            context.fill(x + w - 1, y, x + w, y + h, color);
            return;
        }
        radius = Math.min(radius, Math.min(w, h) / 2);
        context.fill(x + radius, y, x + w - radius, y + 1, color);
        context.fill(x + radius, y + h - 1, x + w - radius, y + h, color);
        context.fill(x, y + radius, x + 1, y + h - radius, color);
        context.fill(x + w - 1, y + radius, x + w, y + h - radius, color);
        for (int i = 0; i < radius; i++) {
            int dy = radius - i;
            int dx = radius - (int) Math.round(Math.sqrt(Math.max(0, radius * radius - dy * dy)));
            context.fill(x + dx, y + i, x + dx + 1, y + i + 1, color);
            context.fill(x + w - dx - 1, y + i, x + w - dx, y + i + 1, color);
            context.fill(x + dx, y + h - i - 1, x + dx + 1, y + h - i, color);
            context.fill(x + w - dx - 1, y + h - i - 1, x + w - dx, y + h - i, color);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click.button() == 0 && uiScale > 0.0f) {
            double designX = (click.x() - offsetX) / uiScale;
            double designY = (click.y() - offsetY) / uiScale;
            int cardX = (DESIGN_WIDTH - CARD_WIDTH) / 2;
            LunaVisualConfig.Feature[] features = LunaVisualConfig.Feature.values();
            for (int i = 0; i < features.length; i++) {
                int cardY = 151 + i * ROW_STEP;
                if (designX >= cardX && designX <= cardX + CARD_WIDTH
                        && designY >= cardY && designY <= cardY + CARD_HEIGHT) {
                    LunaVisualConfig.toggle(features[i]);
                    playClickSound();
                    return true;
                }
            }

            int doneX = (DESIGN_WIDTH - DONE_WIDTH) / 2;
            if (designX >= doneX && designX <= doneX + DONE_WIDTH
                    && designY >= 535 && designY <= 535 + DONE_HEIGHT) {
                playClickSound();
                close();
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }
        if (LunaVisualClient.getOpenSettingsKey() != null
                && LunaVisualClient.getOpenSettingsKey().matchesKey(input)) {
            close();
            return true;
        }
        return super.keyPressed(input);
    }

    private void playClickSound() {
        if (client != null) {
            client.getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        MinecraftClient minecraftClient = client;
        if (minecraftClient != null) {
            minecraftClient.setScreen(parent);
        }
    }
}
