package miyucomics.hattened.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import miyucomics.hattened.misc.PeripheralManager;
import net.minecraft.client.Mouse;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
class MouseMixin {
	@Inject(method = "onMouseButton", at = @At(value = "HEAD"), cancellable = true)
	void interceptClick(long window, int button, int action, int mods, CallbackInfo ci) {
		if (PeripheralManager.shouldIntercept()) {
			PeripheralManager.onClick(button, action);
			ci.cancel();
		}
	}

	@Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), cancellable = true)
	void interceptScroll(long window, double horizontal, double vertical, CallbackInfo ci, @Local Vector2i scroll) {
		if (PeripheralManager.shouldIntercept()) {
			PeripheralManager.onScroll(scroll.y);
			ci.cancel();
		}
	}
}