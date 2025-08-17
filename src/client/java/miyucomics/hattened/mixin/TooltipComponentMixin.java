package miyucomics.hattened.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hattened.item.HatTooltipData;
import miyucomics.hattened.render.HatTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.tooltip.TooltipData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TooltipComponent.class)
interface TooltipComponentMixin {
	@WrapMethod(method = "of(Lnet/minecraft/item/tooltip/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;")
	private static TooltipComponent addHatTooltip(TooltipData tooltipData, Operation<TooltipComponent> original) {
		if (tooltipData instanceof HatTooltipData)
			return new HatTooltipComponent((HatTooltipData) tooltipData);
		return original.call(tooltipData);
	}
}