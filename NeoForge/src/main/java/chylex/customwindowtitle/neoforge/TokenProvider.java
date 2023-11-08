package chylex.customwindowtitle.neoforge;

import chylex.customwindowtitle.TokenException;
import chylex.customwindowtitle.data.CommonTokenProvider;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;

final class TokenProvider implements CommonTokenProvider {
	@Override
	public String getModVersion(final String modId) {
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
