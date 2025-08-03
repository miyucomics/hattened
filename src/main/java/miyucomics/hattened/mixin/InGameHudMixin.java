package miyucomics.hattened.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hattened.inits.HattenedAttachments;
import miyucomics.hattened.misc.ClientStorage;
import miyucomics.hattened.misc.HatData;
import miyucomics.hattened.misc.PlayerEntityRenderStateMinterface;
import miyucomics.hattened.render.HatFeatureRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
class InGameHudMixin {
	@WrapMethod(method = "renderMainHud")
	void noMoreHud(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
		Matrix3x2fStack matrices = context.getMatrices();
		matrices.pushMatrix();
		matrices.translate(0f, (ClientStorage.usingTime / 10f) * 60);
		original.call(context, tickCounter);
		matrices.popMatrix();
	}
}