<div align="center">

# Luna Visual

**See More Clearly.**

<img src="https://img.shields.io/badge/Minecraft-1.21.11-5b8c45?style=flat-square" alt="Minecraft 1.21.11" />
<img src="https://img.shields.io/badge/Fabric-Loom%201.14-d97835?style=flat-square" alt="Fabric Loom 1.14" />
<img src="https://img.shields.io/badge/Java-21-ed8b00?style=flat-square" alt="Java 21" />
<img src="https://img.shields.io/badge/License-MIT-5271a8?style=flat-square" alt="MIT License" />

</div>

**Luna Visual** is a client-side Fabric mod for Minecraft Java Edition that brings four visual enhancement features together in one clean and simple settings screen. Each feature can be enabled or disabled independently.

## ✨ Features

| Feature | Description |
| --- | --- |
| **Full Brightness** | Brightens dark areas without modifying Minecraft's saved gamma settings. |
| **Warden Darkness** | Hides the visual fog and darkening caused by the Darkness effect while preserving its gameplay effects. |
| **Clear Water, Lava, and Powder Snow** | Reduces visual fog when inside water, lava, or powder snow. |
| **Clear Lava Vision** | Reduces lava fog and lowers the fire overlay while in Creative Mode or under the effects of Fire Resistance. |

All features are disabled by default and can be configured independently.

## 📥 Installation

1. Create a Fabric profile for **Minecraft Java Edition 1.21.11**.
2. Install Fabric Loader and a version of Fabric API compatible with Minecraft 1.21.11.
3. Download the Luna Visual JAR file from the **Releases** section of this repository. If no release is available, follow the build instructions below.
4. Place the downloaded JAR file into your Minecraft installation's `mods` folder.
5. Launch Minecraft using your Fabric profile.

> **Note:** Luna Visual is a client-side mod. It does not need to be installed on the server. Make sure you do not have multiple versions of Luna Visual in the same `mods` folder.

## 🎮 Usage

- Press **O** to open the Luna Visual settings screen.
- Use the on-screen buttons to enable or disable individual features.
- Your settings are saved automatically.
- Close the settings screen using the **Done** button or the **Escape** key.
- Change the keybind under **Options → Controls → Key Binds → Luna Visual**.

### Configuration

The configuration file is located at:

```text
config/lunavisual.properties
```

## 🧩 Requirements

- **Minecraft:** Java Edition 1.21.11
- **Fabric Loader:** 0.18.1 or newer
- **Fabric API:** 0.141.1+1.21.11 or newer
- **Java:** 21 or newer

## 🛠️ Building from Source

Make sure Java 21 is installed before building the project.

Run the Gradle Wrapper from the project root directory.

**Windows:**

```powershell
.\gradlew.bat remapJar
```

**macOS / Linux:**

```bash
./gradlew remapJar
```

Once the build completes, the distributable JAR file will be available in:

```text
build/libs/
```

## 📄 License

Luna Visual's source code and visual assets are licensed under the [MIT License](LICENSE).
