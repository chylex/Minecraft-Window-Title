package chylex.customwindowtitle.fabric;
import chylex.customwindowtitle.TitleConfig;
import chylex.customwindowtitle.TitleParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public class CustomWindowTitle implements ClientModInitializer{
	private final TitleConfig config;
	
	public CustomWindowTitle(){
		config = TitleConfig.read(FabricLoader.getInstance().getConfigDirectory().getAbsolutePath());
	}
	
	@Override
	public void onInitializeClient(){
		TokenData.register();
		MinecraftClient.getInstance().execute(this::updateTitle);
	}
	
	private void updateTitle(){
		final Window window = MinecraftClient.getInstance().getWindow();
		window.setTitle(TitleParser.parse(config.getTitle()));
		
		if (config.hasIcon()){
			window.setIcon(config.readIcon16(), config.readIcon32());
		}
	}
}
