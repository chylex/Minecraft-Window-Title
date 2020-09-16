package chylex.customwindowtitle.fabric;
import chylex.customwindowtitle.TitleConfig;
import chylex.customwindowtitle.TitleParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

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
		MinecraftClient.getInstance().getWindow().setTitle(TitleParser.parse(config.getTitle()));
	}
}
