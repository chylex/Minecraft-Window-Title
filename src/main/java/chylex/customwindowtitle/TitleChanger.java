package chylex.customwindowtitle;

import net.minecraft.client.Minecraft;

public final class TitleChanger {
	private TitleChanger() {}
	
	public static void setTitle(TitleConfig config) {
		Minecraft.getInstance().getWindow().setTitle(TitleParser.parse(config.getTitle()));
	}
}
