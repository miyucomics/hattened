package miyucomics.hattened.mixin;

import miyucomics.hattened.attach.HatData;
import miyucomics.hattened.attach.HatState;
import miyucomics.hattened.inits.HattenedAttachments;
import miyucomics.hattened.misc.PlayerEntityRenderStateMinterface;
import miyucomics.hattened.render.HatFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
class PlayerEntityRendererMixin {
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "<init>", at = @At("TAIL"))
	void addHatFeature(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
		//noinspection DataFlowIssue
		((LivingEntityRenderer) (Object) this).addFeature(new HatFeatureRenderer((PlayerEntityRenderer) (Object) this));
	}

	@SuppressWarnings("UnstableApiUsage")
	@Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("TAIL"))
	void addExtraPose(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float tickDelta, CallbackInfo ci) {
		((PlayerEntityRenderStateMinterface) state).setHat(player.getAttachedOrElse(HattenedAttachments.HAT_DATA, HatData.DEFAULT));
		((PlayerEntityRenderStateMinterface) state).setHatState(player.getAttachedOrElse(HattenedAttachments.HAT_STATE_DATA, HatState.DEFAULT));
	}
}