package chylex.customwindowtitle.fabric;
import chylex.customwindowtitle.TokenException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import static chylex.customwindowtitle.TitleTokens.noArgs;
import static chylex.customwindowtitle.TitleTokens.oneArg;
import static chylex.customwindowtitle.TitleTokens.registerToken;

final class TokenData{
	static void register(){
		registerToken("mcversion", noArgs(TokenData::getMinecraftVersion));
		registerToken("modversion", oneArg(TokenData::getModVersion));
	}
	
	static String getMinecraftVersion(){
		return SharedConstants.getGameVersion().getName();
	}
	
	static String getModVersion(final String modId){
		return FabricLoader.getInstance().getModContainer(modId).orElseThrow(() -> new TokenException("mod info for '" + modId + "' not found")).getMetadata().getVersion().getFriendlyString();
	}
}
