package chylex.customwindowtitle.data;
import net.minecraft.client.Minecraft;

public interface CommonTokenProvider {
	default String getMinecraftVersion() {
		return Minecraft.getInstance().getGame().getVersion().getName();
	}
	
	String getModVersion(final String modId);
}
