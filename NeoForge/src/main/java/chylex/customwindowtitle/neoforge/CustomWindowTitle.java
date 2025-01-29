package chylex.customwindowtitle.neoforge;

import chylex.customwindowtitle.TitleChanger;
import chylex.customwindowtitle.TitleConfig;
import chylex.customwindowtitle.data.CommonTokenData;
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
	}
	
	@SubscribeEvent
	public void onClientSetup(final FMLClientSetupEvent e) {
		CommonTokenData.register(new TokenProvider());
		e.enqueueWork(() -> TitleChanger.setTitle(config));
	}
}
