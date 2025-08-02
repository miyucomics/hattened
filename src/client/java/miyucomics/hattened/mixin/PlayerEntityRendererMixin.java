package miyucomics.hattened.mixin;

import miyucomics.hattened.render.HatFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
class PlayerEntityRendererMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	void addHatFeature(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
		((LivingEntityRenderer) (Object) this).addFeature(new HatFeatureRenderer((PlayerEntityRenderer) (Object) this));
	}
}