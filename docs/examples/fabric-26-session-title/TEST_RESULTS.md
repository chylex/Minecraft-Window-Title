# Minecraft 26.3 account-prefixed title test

Tested October 8, 2026 on Windows with Minecraft 26.3, Fabric Loader 0.19.5,
Java 25.0.1 and three separate authenticated Prism instances. Their client mod
stack included Iris 1.11.7, Sodium 0.9.3-alpha.1, ReplayMod 2.6.27 and Replay
Exporter 1.4.1. The independently implemented title mod was added to all three.

Observed actual Win32 titles, with distinct account names:

```
EmmaAI â€” Minecraft* 26.3 - Multiplayer (3rd-party Server)
BrandonAI â€” Minecraft* 26.3 - Multiplayer (3rd-party Server)
ElricHeart â€” Minecraft* 26.3 - Multiplayer (3rd-party Server)
```

For Emma's same running process, an intentional disconnect yielded
`EmmaAI â€” Minecraft* 26.3`, and reconnecting restored the multiplayer suffix
while keeping the account prefix. The other two clients remained connected.
All three names were present again after reconnect and a subsequent stability
check. No external title-renaming timer was used.

The initial account-prefixed title was also recorded by the mod at window
construction, before joining. The generated JAR's SHA-256 was:

`fb621df6d008e05b69c57dafa53ad937764057643d4fc3dfefd0ef679d012609`

Implementation: required RETURN injection into `Minecraft.createTitle()`,
prefixing `Minecraft.getUser().getName()` to the vanilla return value. This
preserves Minecraft's version/modded/connection suffix and covers both window
construction and later `Minecraft.updateTitle()` calls.

Inspection of the installed 26.3 client confirmed that `Window.setTitle(String)`
delegates to `org.lwjgl.sdl.SDLVideo.SDL_SetWindowTitle`. The solution uses
Minecraft's title generation rather than directly calling a window backend.

This is a title-only compatibility proof, not a complete port of Custom Window
Title. Its configuration/token parser, icon support, NeoForge and non-Windows
platforms have not been tested. The upstream `TitleChanger` already uses
`Minecraft.getWindow().setTitle(...)`, which remains available; upstream
`IconChanger` uses GLFW directly and needs a separate SDL adaptation.

## Version 1.1.0 compatibility expansion

One broadened JAR was tested in isolated menu-only clients:

- 26.1, 26.1.1 and 26.1.2: Fabric 0.18.4, GLFW; correct account-prefixed Win32
  title and clean exit.
- 26.2: Fabric 0.19.5, GLFW; correct title and clean exit.
- 26.3: Fabric 0.19.5, SDL; correct title and clean exit.
- 26.4 Snapshot 3: Fabric 0.19.5, SDL; correct title observed, followed by a
  graphics/resource startup failure. A no-title-mod control also failed.
  Vulkan and an explicit OpenGL preference were tried. Full client stability
  and the future final 26.4 release remain unverified.

SHA-256: `1ad0ce7e028049931f7136a321ccda514ccc8d00a13fb3639c5f89c8364ce0bc`.
The Mixin class bytes match 1.0.0; this release broadens version/loader metadata
and builds against the oldest supported client. No production worlds or account
stores were copied. This example includes the shareable results; machine-local receipts and client binaries are omitted.
