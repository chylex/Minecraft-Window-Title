package chylex.customwindowtitle.forge;
import chylex.customwindowtitle.TitleConfig;
import chylex.customwindowtitle.TitleParser;
import chylex.customwindowtitle.data.CommonTokenData;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;

@Mod("customwindowtitle")
public class CustomWindowTitle {
	private final TitleConfig config;
	
	public CustomWindowTitle() {
		config = TitleConfig.read(FMLPaths.CONFIGDIR.get().toString());
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
		CommonTokenData.register(new TokenProvider());
	}
	
	@SubscribeEvent
	public void onClientSetup(final FMLClientSetupEvent e) {
		e.enqueueWork(this::updateTitle);
	}
	
	private void updateTitle() {
		final Window window = Minecraft.getInstance().getWindow();
		window.setTitle(TitleParser.parse(config.getTitle()));
		
		if (config.hasIcon()) {
			window.setIcon(config.readIcon16(), config.readIcon32());
		}
	}
}
