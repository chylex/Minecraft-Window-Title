package chylex.customwindowtitle;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class IconChanger {
	private IconChanger() {}
	
	public static void setIcon(Path iconPath) {
		long windowHandle = Minecraft.getInstance().getWindow().getWindow();
		setWindowIcon(windowHandle, iconPath);
	}
	
	private static void setWindowIcon(long windowHandle, Path iconPath) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);
			
			ByteBuffer icon = loadIcon(iconPath, w, h, channels);
			if (icon == null) {
				return;
			}
			
			try (GLFWImage glfwImage1 = GLFWImage.malloc(); GLFWImage glfwImage2 = GLFWImage.malloc(); GLFWImage.Buffer icons = GLFWImage.malloc(2)) {
				glfwImage1.set(w.get(0), h.get(0), icon);
				glfwImage2.set(w.get(0), h.get(0), icon);
				
				icons.put(0, glfwImage1);
				icons.put(1, glfwImage2);
				
				GLFW.glfwSetWindowIcon(windowHandle, icons);
			}
		} catch (Exception e) {
			System.err.println("Failed to set window icon: " + iconPath);
			e.printStackTrace();
		}
	}
	
	private static ByteBuffer loadIcon(Path path, IntBuffer w, IntBuffer h, IntBuffer channels) throws IOException {
		byte[] iconBytes = Files.readAllBytes(path);
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(iconBytes.length).put(iconBytes).flip();
		ByteBuffer icon = STBImage.stbi_load_from_memory(buffer, w, h, channels, 4);
		
		if (icon == null) {
			System.err.println("Failed to load image from memory for: " + path + " - " + STBImage.stbi_failure_reason());
		}
		
		return icon;
	}
}
