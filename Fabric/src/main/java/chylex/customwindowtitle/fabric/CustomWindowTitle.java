package chylex.customwindowtitle.fabric;

import chylex.customwindowtitle.TitleChanger;
import chylex.customwindowtitle.TitleConfig;
import chylex.customwindowtitle.data.CommonTokenData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class CustomWindowTitle implements ClientModInitializer {
	private final TitleConfig config;
	
	public CustomWindowTitle() {
		config = TitleConfig.load(FabricLoader.getInstance().getConfigDir().toAbsolutePath().toString());
	}
	
	@Override
	public void onInitializeClient() {
		CommonTokenData.register(new TokenProvider());
		Minecraft.getInstance().execute(() -> TitleChanger.setTitle(config));
	}
}
