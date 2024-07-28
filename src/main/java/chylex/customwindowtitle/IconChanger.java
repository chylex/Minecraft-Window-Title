package chylex.customwindowtitle;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class IconChanger {

    public static void setIcon(final Path iconPath) {
        final long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        setWindowIcon(windowHandle, iconPath);
    }

    private static void setWindowIcon(final long windowHandle, final Path iconPath) {
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer w = stack.mallocInt(1);
            final IntBuffer h = stack.mallocInt(1);
            final IntBuffer channels = stack.mallocInt(1);

            final ByteBuffer icon = loadIcon(iconPath, w, h, channels);
            if (icon != null) {
                final GLFWImage glfwImage1 = GLFWImage.malloc();
                glfwImage1.set(w.get(0), h.get(0), icon);
                final GLFWImage glfwImage2 = GLFWImage.malloc();
                glfwImage2.set(w.get(0), h.get(0), icon);

                final GLFWImage.Buffer icons = GLFWImage.malloc(2);
                icons.put(0, glfwImage1);
                icons.put(1, glfwImage2);

                org.lwjgl.glfw.GLFW.glfwSetWindowIcon(windowHandle, icons);

                icons.free();
                glfwImage1.free();
                glfwImage2.free();
            } else {
                System.err.println("Failed to load icon: " + iconPath);
            }
        }
    }

    private static ByteBuffer loadIcon(final Path path, final IntBuffer w, final IntBuffer h, final IntBuffer channels) {
        try (final InputStream inputStream = Files.newInputStream(path)) {
            final byte[] iconBytes = inputStream.readAllBytes();
            final ByteBuffer buffer = ByteBuffer.allocateDirect(iconBytes.length).put(iconBytes).flip();
            final ByteBuffer icon = STBImage.stbi_load_from_memory(buffer, w, h, channels, 4);
            if (icon == null) {
                System.err.println("Failed to load image from memory for: " + path + " - " + STBImage.stbi_failure_reason());
            }
            return icon;
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

