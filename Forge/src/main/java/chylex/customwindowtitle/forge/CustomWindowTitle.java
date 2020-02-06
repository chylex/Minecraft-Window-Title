package chylex.customwindowtitle.forge;
import chylex.customwindowtitle.TitleParser;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.Display;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Mod(modid = "customwindowtitle", useMetadata = true, clientSideOnly = true, acceptedMinecraftVersions = "*", acceptableRemoteVersions = "*")
public class CustomWindowTitle{
	private static final String defaultTitle = "Minecraft {mcversion}";
	private String configTitle;
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e){
		Path configFile = Paths.get(e.getModConfigurationDirectory().getAbsolutePath(), "customwindowtitle-client.toml");
		
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
		}catch(IOException ex){
			throw new RuntimeException("CustomWindowTitle configuration error", ex);
		}
		
		TokenData.register();
		updateTitle();
	}
	
	private void updateTitle(){
		Display.setTitle(TitleParser.parse(configTitle));
	}
}
