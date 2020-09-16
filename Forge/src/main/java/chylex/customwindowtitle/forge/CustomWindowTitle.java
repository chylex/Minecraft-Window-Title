package chylex.customwindowtitle.forge;
import chylex.customwindowtitle.TitleParser;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("customwindowtitle")
public class CustomWindowTitle{
	private final ConfigValue<String> configTitle;
	
	public CustomWindowTitle(){
		final ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		
		configTitle = configBuilder.define("title", "Minecraft {mcversion}");
		
		ModLoadingContext.get().registerConfig(Type.CLIENT, configBuilder.build());
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		
		TokenData.register();
	}
	
	@SubscribeEvent
	public void onClientSetup(final FMLClientSetupEvent e){
		e.getMinecraftSupplier().get().execute(this::updateTitle);
	}
	
	private void updateTitle(){
		Minecraft.getInstance().getMainWindow().func_230148_b_(TitleParser.parse(configTitle.get()));
	}
}
