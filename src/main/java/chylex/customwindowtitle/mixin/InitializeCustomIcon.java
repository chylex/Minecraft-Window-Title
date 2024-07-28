package chylex.customwindowtitle.mixin;

import chylex.customwindowtitle.IconChanger;
import chylex.customwindowtitle.TitleConfig;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public final class InitializeCustomIcon {
	@Inject(method = "onResourceLoadFinished", at = @At("HEAD"))
	private void onFinishedLoading(CallbackInfo callbackInfo) {
		TitleConfig config = TitleConfig.getInstance();
		if (config != null && config.hasIcon()) {
			IconChanger.setIcon(config.getIconPath());
		}
	}
}
