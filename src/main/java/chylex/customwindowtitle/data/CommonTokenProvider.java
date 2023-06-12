package chylex.customwindowtitle.data;

import net.minecraft.SharedConstants;

public interface CommonTokenProvider {
	default String getMinecraftVersion() {
		return SharedConstants.getCurrentVersion().getName();
	}
	
	String getModVersion(final String modId);
}
