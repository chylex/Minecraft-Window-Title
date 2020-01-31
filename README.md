# For Users

## Installation

The mod supports [Forge](https://files.minecraftforge.net/) and [Fabric](https://fabricmc.net/) (*) mod loaders, **the same mod file will work in both**. Follow either mod loader's installation guide, then [download the mod file](https://www.curseforge.com/minecraft/mc-mods/custom-window-title/files) for your Minecraft version, and install it into the **.minecraft/mods** folder.

(*) Fabric API is not required.

## Configuration

Run the game once to create the configuration file. By default, the window title will be set to **Minecraft _<version>_**. Unlike in vanilla 1.15.2 onwards, the title will not change when you enter a world/server.

To change the title, navigate to the **.minecraft/config** folder, and open **customwindowtitle-client.toml** in a text editor. You will see the following line:

```toml
title = "Minecraft {mcversion}"
```

Change only text inside the quotes. You can use the following special tokens:

* **{mcversion}** - current Minecraft version
* **{modversion:<span style="text-decoration: underline;">modid</span>}** - version of installed mod with the identifier _modid_

If any of the tokens aren't working, search the game log for **CustomWindowTitle** and you should see the reason, otherwise please file an issue on the [issue tracker](https://github.com/chylex/Minecraft-Window-Title/issues) with as many details as possible.

## Screenshots

These screenshots were taken using the following example configuration:

```toml
title = "Minecraft {mcversion} - Custom Window Title {modversion:customwindowtitle}"
```

![](https://media.forgecdn.net/attachments/277/212/customwindowtitlefabric.png)![](https://media.forgecdn.net/attachments/277/213/customwindowtitleforge.png)

# For Developers

Currently the common source files (package `chylex.customwindowtitle`) are just duplicated in the Forge and Fabric folders, and the jar files are combined with a horrible python script. If anyone has the time and patience to clean it up, I'd appreciate a Pull Request :P

For now, you can build either separate Forge and Fabric projects using `gradlew build` in either folder, or run `python BuildMerge.py` to automatically build both and combine them into a single `.jar` file.
