package chylex.customwindowtitle.forge;
import chylex.customwindowtitle.TokenException;
import net.minecraft.SharedConstants;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import static chylex.customwindowtitle.TitleTokens.noArgs;
import static chylex.customwindowtitle.TitleTokens.oneArg;
import static chylex.customwindowtitle.TitleTokens.registerToken;

final class TokenData {
	static void register() {
		registerToken("mcversion", noArgs(TokenData::getMinecraftVersion));
		registerToken("modversion", oneArg(TokenData::getModVersion));
	}
	
	static String getMinecraftVersion() {
		return SharedConstants.getCurrentVersion().getName();
	}
	
	static String getModVersion(final String modId) {
		final IModFileInfo file = ModList.get().getModFileById(modId);
		
		if (file == null) {
			throw new TokenException("mod file for '" + modId + "' not found");
		}
		
		for (final IModInfo info : file.getMods()) {
			if (info.getModId().equals(modId)) {
				return info.getVersion().toString();
			}
		}
		
		throw new TokenException("mod info for '" + modId + "' not found");
	}
}
