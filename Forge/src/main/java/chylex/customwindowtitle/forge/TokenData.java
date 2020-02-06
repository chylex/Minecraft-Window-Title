package chylex.customwindowtitle.forge;
import chylex.customwindowtitle.TokenException;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import static chylex.customwindowtitle.TitleTokens.noArgs;
import static chylex.customwindowtitle.TitleTokens.oneArg;
import static chylex.customwindowtitle.TitleTokens.registerToken;

final class TokenData{
	static void register(){
		registerToken("mcversion", noArgs(TokenData::getMinecraftVersion));
		registerToken("modversion", oneArg(TokenData::getModVersion));
	}
	
	static String getMinecraftVersion(){
		return RealmsSharedConstants.VERSION_STRING;
	}
	
	static String getModVersion(String modId){
		ModContainer mod = Loader.instance().getIndexedModList().get(modId);
		
		if (mod == null){
			throw new TokenException("mod info for '" + modId + "' not found");
		}
		
		return mod.getMetadata().version;
	}
}
