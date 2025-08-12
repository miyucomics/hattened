package miyucomics.hattened.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hattened.render.HatAbilityMenu;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
class InGameHudMixin {
	@WrapMethod(method = "renderMainHud")
	void noMoreHud(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
//		if (!PeripheralManager.shouldIntercept() && ClientStorage.usingTime == 0) {
//			original.call(context, tickCounter);
//			return;
//		}

//		float progress = ClientStorage.getSmoothUsingTime(tickCounter);

		Matrix3x2fStack matrices = context.getMatrices();
		matrices.pushMatrix();
//		matrices.translate(0f, progress * 60f);
		original.call(context, tickCounter);
		matrices.popMatrix();

		HatAbilityMenu.render(context, 1f);
	}
}