package chylex.customwindowtitle.forge;
import chylex.customwindowtitle.TitleConfig;
import chylex.customwindowtitle.TitleParser;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("customwindowtitle")
public class CustomWindowTitle{
	private final TitleConfig config;
	
	public CustomWindowTitle(){
		config = TitleConfig.read(FMLPaths.CONFIGDIR.get().toString());
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		TokenData.register();
	}
	
	@SubscribeEvent
	public void onClientSetup(final FMLClientSetupEvent e){
		e.getMinecraftSupplier().get().execute(this::updateTitle);
	}
	
	private void updateTitle(){
		final MainWindow window = Minecraft.getInstance().getMainWindow();
		window.func_230148_b_(TitleParser.parse(config.getTitle()));
		
		if (config.hasIcon()){
			window.setWindowIcon(config.readIcon16(), config.readIcon32());
		}
	}
}
