package chylex.customwindowtitle.neoforge;

import chylex.customwindowtitle.TitleConfig;
import chylex.customwindowtitle.TitleParser;
import chylex.customwindowtitle.data.CommonTokenData;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;

@Mod("customwindowtitle")
public class CustomWindowTitle {
	
	private final TitleConfig config;
	
	public CustomWindowTitle(IEventBus eventBus) {
		config = TitleConfig.load(FMLPaths.CONFIGDIR.get().toString());
		eventBus.addListener(this::onClientSetup);
		CommonTokenData.register(new TokenProvider());
	}
	
	@SubscribeEvent
	public void onClientSetup(final FMLClientSetupEvent e) {
		e.enqueueWork(this::updateTitle);
	}
	
	private void updateTitle() {
		Minecraft.getInstance().getWindow().setTitle(TitleParser.parse(config.getTitle()));
	}
}
