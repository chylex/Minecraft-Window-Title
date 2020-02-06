package chylex.customwindowtitle.fabric;
import chylex.customwindowtitle.TitleParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class CustomWindowTitle implements ClientModInitializer{
	private static final String defaultTitle = "Minecraft {mcversion}";
	private String configTitle;
	
	@Override
	public void onInitializeClient(){
		Path configFile = Paths.get(FabricLoader.getInstance().getConfigDirectory().getAbsolutePath(), "customwindowtitle-client.toml");
		
		try{
			String prefix = "title = ";
			
			if (!Files.exists(configFile)){
				Files.write(configFile, Collections.singletonList(prefix + '"' + defaultTitle + '"'), StandardCharsets.UTF_8);
				configTitle = defaultTitle;
			}
			else{
				configTitle = Files
					.readAllLines(configFile, StandardCharsets.UTF_8)
					.stream()
					.filter(line -> line.startsWith(prefix))
					.map(line -> StringUtils.strip(StringUtils.removeStart(line, prefix).trim(), "\""))
					.findFirst()
					.orElse(defaultTitle);
			}
		}catch(IOException e){
			throw new RuntimeException("CustomWindowTitle configuration error", e);
		}
		
		TokenData.register();
		MinecraftClient.getInstance().method_18858(this::updateTitle);
	}
	
	private void updateTitle(){
		GLFW.glfwSetWindowTitle(MinecraftClient.getInstance().window.getHandle(), TitleParser.parse(configTitle));
	}
}
