package chylex.customwindowtitle.fabric;

import chylex.customwindowtitle.TokenException;
import chylex.customwindowtitle.data.CommonTokenProvider;
import net.fabricmc.loader.api.FabricLoader;

final class TokenProvider implements CommonTokenProvider {
	@Override
	public String getModVersion(final String modId) {
		return FabricLoader.getInstance()
			.getModContainer(modId)
			.orElseThrow(() -> new TokenException("mod info for '" + modId + "' not found"))
			.getMetadata()
			.getVersion()
			.getFriendlyString();
	}
}
