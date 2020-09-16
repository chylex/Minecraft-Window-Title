package chylex.customwindowtitle.fabric;
import chylex.customwindowtitle.TitleParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.StringUtils;
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
		final Path configFile = Paths.get(FabricLoader.getInstance().getConfigDirectory().getAbsolutePath(), "customwindowtitle-client.toml");
		
		try{
			final String prefix = "title = ";
			
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
		}catch(final IOException e){
			throw new RuntimeException("CustomWindowTitle configuration error", e);
		}
		
		TokenData.register();
		MinecraftClient.getInstance().execute(this::updateTitle);
	}
	
	private void updateTitle(){
		MinecraftClient.getInstance().getWindow().setTitle(TitleParser.parse(configTitle));
	}
}
