package dev.lunavisual.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class LunaVisualConfig {
    private static final Path CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("lunavisual.properties");

    public static boolean fullbright;
    public static boolean wardenDarkness;
    public static boolean clearFluids;
    public static boolean lavaClearView;

    private LunaVisualConfig() {
    }

    public static void load() {
        Properties properties = new Properties();
        if (Files.isRegularFile(CONFIG_FILE)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {
                properties.load(reader);
            } catch (IOException exception) {
                System.err.println("[Luna Visual] Could not read config: " + exception.getMessage());
            }
        }

        fullbright = readBoolean(properties, "fullbright", false);
        wardenDarkness = readBoolean(properties, "wardenDarkness", false);
        clearFluids = readBoolean(properties, "clearFluids", false);
        lavaClearView = readBoolean(properties, "lavaClearView", false);
        save();
    }

    public static void toggle(Feature feature) {
        switch (feature) {
            case FULLBRIGHT -> fullbright = !fullbright;
            case WARDEN_DARKNESS -> wardenDarkness = !wardenDarkness;
            case CLEAR_FLUIDS -> clearFluids = !clearFluids;
            case LAVA_CLEAR_VIEW -> lavaClearView = !lavaClearView;
        }
        save();
    }

    public static boolean isEnabled(Feature feature) {
        return switch (feature) {
            case FULLBRIGHT -> fullbright;
            case WARDEN_DARKNESS -> wardenDarkness;
            case CLEAR_FLUIDS -> clearFluids;
            case LAVA_CLEAR_VIEW -> lavaClearView;
        };
    }

    private static boolean readBoolean(Properties properties, String key, boolean fallback) {
        String value = properties.getProperty(key);
        return value == null ? fallback : Boolean.parseBoolean(value);
    }

    private static void save() {
        Properties properties = new Properties();
        properties.setProperty("fullbright", Boolean.toString(fullbright));
        properties.setProperty("wardenDarkness", Boolean.toString(wardenDarkness));
        properties.setProperty("clearFluids", Boolean.toString(clearFluids));
        properties.setProperty("lavaClearView", Boolean.toString(lavaClearView));

        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_FILE)) {
                properties.store(writer, "Luna Visual client visual options");
            }
        } catch (IOException exception) {
            System.err.println("[Luna Visual] Could not save config: " + exception.getMessage());
        }
    }

    public enum Feature {
        FULLBRIGHT("feature.lunavisual.fullbright", "feature.lunavisual.fullbright.description"),
        WARDEN_DARKNESS("feature.lunavisual.warden_darkness", "feature.lunavisual.warden_darkness.description"),
        CLEAR_FLUIDS("feature.lunavisual.clear_fluids", "feature.lunavisual.clear_fluids.description"),
        LAVA_CLEAR_VIEW("feature.lunavisual.lava_clear_view", "feature.lunavisual.lava_clear_view.description");

        private final String titleKey;
        private final String descriptionKey;

        Feature(String titleKey, String descriptionKey) {
            this.titleKey = titleKey;
            this.descriptionKey = descriptionKey;
        }

        public String titleKey() {
            return titleKey;
        }

        public String descriptionKey() {
            return descriptionKey;
        }
    }
}
