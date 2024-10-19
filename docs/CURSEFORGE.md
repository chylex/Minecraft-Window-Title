## Installation

The mod supports [NeoForge](https://neoforged.net/) and [Fabric](https://fabricmc.net/) (*) mod loaders. Follow either mod loader's installation guide, then [download the mod file](https://www.curseforge.com/minecraft/mc-mods/custom-window-title/files) for your Minecraft version, and install it into the `.minecraft/mods` folder.

(*) Fabric API is **not** required.

## Configuration

Run the game once to create the configuration file. By default, the window title will be set to **Minecraft _<version>_**. Unlike in vanilla 1.15.2 onwards, the title will not change when you enter a world/server.

To change the title or icon, navigate to the `.minecraft/config` folder, and open `customwindowtitle-client.toml` in a text editor. You will see the following entries:

```toml
title = 'Minecraft {mcversion}'  
icon16 = ''  
icon32 = ''
```

Only edit text inside quotes or apostrophes.

### Changing the Title

You can use the following special tokens in the _title_ configuration entry:

* `{mcversion}` - current Minecraft version
* `{modversion:mod_id}` - version of installed mod with the identifier `mod_id`
* `{username}` - current username

If any of the tokens aren't working, search the game log for **CustomWindowTitle** and you should see the reason, otherwise please file an issue on the [issue tracker](https://github.com/chylex/Minecraft-Window-Title/issues) with as many details as possible.

### Changing the Icon

#### Minecraft 1.21+

**This feature is only available in Custom Window Title 1.4.0 and newer.**

Create a square PNG image whose dimensions are a power of two, such as 32x32 or 48x48. Put the PNG file into the `.minecraft/config` folder, either directly or into a subfolder.

The icon **must be saved with transparency** even if it don't use it, otherwise the icon may be corrupted or not appear at all. In Krita for example, you must check _Store alpha channel (transparency)_ when saving.

The _icon_ configuration entry points to the PNG file relative to `.minecraft/config`.

For example, if you placed the icon into `.minecraft/config/customwindowtitle/icon.png`, then the configuration entry should look like this:

```toml
icon = 'customwindowtitle/icon.png'
```

#### Minecraft 1.20

This feature is not available in Minecraft 1.20.

#### Minecraft 1.19 and older

Instead of one `icon` configuration entry, there are two configuration entries `icon16` and `icon32` for icons with dimensions 16x16 and 32x32.

## Screenshots

These screenshots were taken using the following example configuration:

```toml
title = "Minecraft {mcversion} - Custom Window Title {modversion:customwindowtitle}"
```

<img src="https://media.forgecdn.net/attachments/277/212/customwindowtitlefabric.png" width="417" height="284"><img src="https://media.forgecdn.net/attachments/277/213/customwindowtitleforge.png" alt="" width="417" height="284">
