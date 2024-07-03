package chylex.customwindowtitle.data;

import static chylex.customwindowtitle.TitleTokens.noArgs;
import static chylex.customwindowtitle.TitleTokens.oneArg;
import static chylex.customwindowtitle.TitleTokens.registerToken;

public final class CommonTokenData {
	public static void register(final CommonTokenProvider provider) {
		registerToken("mcversion", noArgs(provider::getMinecraftVersion));
		registerToken("modversion", oneArg(provider::getModVersion));
		registerToken("username", noArgs(provider::getUsername));
	}
	
	private CommonTokenData() {}
}
