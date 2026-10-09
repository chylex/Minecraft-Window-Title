# Minecraft 26.x session-title reference

This standalone Fabric example distinguishes multiple Minecraft windows by
prefixing the current session username to the game's generated title:

```text
ExamplePlayer — Minecraft* 26.3 - Multiplayer (3rd-party Server)
```

It is an independently implemented, title-only compatibility reference for
[the 26.x discussion](https://github.com/chylex/Minecraft-Window-Title/issues/24).
It is not a release or complete port of Custom Window Title. It does not
implement its configuration, tokens, custom icons, or NeoForge integration.
The example has its own mod ID and MIT license; upstream's existing license
and production source remain unchanged. Do not install it alongside another
mod that replaces title generation, including Custom Window Title.

## Why the title survives game updates

The required RETURN injection into `Minecraft.createTitle()` prefixes
`Minecraft.getUser().getName()` to the original return value. This covers
initial window creation and later `Minecraft.updateTitle()` calls, preserving
Minecraft's normal version, modded status and connection suffix.

It uses Minecraft's title abstraction, without calling GLFW or SDL directly.
The inspected 26.3 `Window.setTitle(String)` delegates to
`SDLVideo.SDL_SetWindowTitle`. This example does not address upstream's direct
GLFW icon handling.

## Build

Requires Python 3, a Java 25 JDK, and an existing Prism library with the
Minecraft 26.1 client and Fabric Mixin `0.17.0+mixin.0.8.7`. The script does not
download dependencies, read account stores, launch clients or install the mod.
Run from this example directory:

```powershell
python build.py
```

On Windows, the default Prism location is `%APPDATA%/PrismLauncher` and the
default JDK is its `java/java-runtime-epsilon`. Supply paths when necessary:

```text
python build.py --prism-root /path/to/PrismLauncher --jdk /path/to/java25
```

`--minecraft VERSION` selects another installed compilation client;
`--client-jar PATH` supplies a client JAR explicitly. The build uses `javac`
directly because these clients contain unobfuscated classes. Build artifacts
and machine-local compiler arguments are ignored by Git.

Output: `build/studio-window-title-1.1.0+mc26.jar`. The receipt in `build/`
records the artifact hash and compilation client. Install only this output
JAR in a client's mods directory, then start Minecraft. Fabric API and a
server-side mod are unnecessary.

## Verified scope

The same JAR was tested on Windows on October 8, 2026:

| Minecraft | Fabric Loader | Backend | Observed result |
|---|---|---|---|
| 26.1 | 0.18.4 | GLFW | Correct title; clean exit |
| 26.1.1 | 0.18.4 | GLFW | Correct title; clean exit |
| 26.1.2 | 0.18.4 | GLFW | Correct title; clean exit |
| 26.2 | 0.19.5 | GLFW | Correct title; clean exit |
| 26.3 | 0.19.5 | SDL | Correct title; clean exit |
| 26.4 Snapshot 3 | 0.19.5 | SDL | Correct title; subsequent startup failure |

The version matrix used isolated offline menu sessions with only Fabric and
this example. The identical Mixin class was also tested in three simultaneous
authenticated 26.3 clients with Iris/Sodium, ReplayMod and Replay Exporter.
An intentional disconnect/reconnect retained the username in the same process.
See [TEST_RESULTS.md](TEST_RESULTS.md) for those results and artifact hashes.

26.4 Snapshot 3 remains provisional: both the title example and a control
without it failed during graphics/resource startup on the test PC. The title
hook was observed, but stable snapshot gameplay and a future final 26.4 release
are unverified. The declared range `>=26.1 <26.5` is an intended compatibility
range, not a claim that every release in that range has passed testing.

Other operating systems, icons, configuration/token compatibility and NeoForge
were not tested. No Minecraft binaries, game worlds, account data or local
installation receipts are included.
