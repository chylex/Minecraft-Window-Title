package chylex.customwindowtitle.mixin;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public final class DisableVanillaTitle {
	@Inject(method = "updateTitle()V", at = @At("HEAD"), cancellable = true)
	private void updateTitle(final CallbackInfo info) {
		info.cancel();
	}
}
