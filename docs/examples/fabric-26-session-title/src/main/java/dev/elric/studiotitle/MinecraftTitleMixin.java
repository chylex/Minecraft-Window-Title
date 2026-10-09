package dev.elric.studiotitle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Adds identity whenever Minecraft generates a title, including startup. */
@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftTitleMixin {
    @Inject(method = "createTitle", at = @At("RETURN"), cancellable = true, require = 1)
    private void studioTitle$includeAccount(CallbackInfoReturnable<String> result) {
        User user = ((Minecraft) (Object) this).getUser();
        if (user != null && !user.getName().isBlank()) {
            String title = user.getName() + " — " + result.getReturnValue();
            result.setReturnValue(title);
            System.out.println("[StudioWindowTitle] " + title);
        }
    }
}
