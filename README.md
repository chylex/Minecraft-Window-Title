# For Users

## Installation

The mod supports [Forge](https://files.minecraftforge.net/) and [Fabric](https://fabricmc.net/) (*) mod loaders, **the same mod file will work in both**. Follow either mod loader's installation guide, then [download the mod file](https://www.curseforge.com/minecraft/mc-mods/custom-window-title/files) for your Minecraft version, and install it into the **.minecraft/mods** folder.

(*) Fabric API is **not** required.

## Configuration

Run the game once to create the configuration file. By default, the window title will be set to **Minecraft _<version>_**. Unlike in vanilla 1.15.2 onwards, the title will not change when you enter a world/server.

To change the title or icon, navigate to the **.minecraft/config** folder, and open **customwindowtitle-client.toml** in a text editor. You will see the following entries:

```toml
title = 'Minecraft {mcversion}'  
icon16 = ''  
icon32 = ''
```

Only edit text inside quotes or apostrophes.

### Changing the Title

You can use the following special tokens in the _title_ configuration entry:

* **{mcversion}** - current Minecraft version
* **{modversion:<span style="text-decoration: underline;">modid</span>}** - version of installed mod with the identifier _modid_

If any of the tokens aren't working, search the game log for **CustomWindowTitle** and you should see the reason, otherwise please file an issue on the [issue tracker](https://github.com/chylex/Minecraft-Window-Title/issues) with as many details as possible.

### Changing the Icon

**This feature is currently not supported in Minecraft 1.20.**

You must create two PNG images with sizes 16x16 and 32x32 pixels. The images **must be saved with transparency** even if they don't use it, **otherwise the icons will appear corrupted**. In Krita for example, you must check _Store alpha channel (transparency)_ when saving the image.

The _icon16_ and _icon32_ configuration entries point to the PNG files relative to the **.minecraft/config** folder. For example, if you place the two icons in a folder named _customwindowtitle_ as follows:

* `.minecraft/config/customwindowtitle-client.toml`
* `.minecraft/config/customwindowtitle/icon16.png`
* `.minecraft/config/customwindowtitle/icon32.png`

Then, the two icon entries should look like this:

```toml
icon16 = 'customwindowtitle/icon16.png'  
icon32 = 'customwindowtitle/icon32.png'
```

## Screenshots

These screenshots were taken using the following example configuration:

```toml
title = "Minecraft {mcversion} - Custom Window Title {modversion:customwindowtitle}"
```

![](https://github.com/chylex/Minecraft-Window-Title/blob/master/.github/README/screenshot.png)

# For Developers

The mod sources are organized into 3 projects:
- `src/` contains common source files and mixins
- `Fabric/src/` contains source files specific for Fabric
- `Forge/src/` contains source files specific for Forge

The Gradle project provides the following tasks:
- `setupIdea` generates Minecraft sources and run configurations for IntelliJ IDEA
- `assemble` creates 2 `.jar` files in the `build/dist` folder - one for Forge, one for Fabric

When building against a Minecraft version that is only supported by one mod loader, open `gradle.properties` and comment or remove either `forgeVersion` or `fabricVersion` to disable them.
