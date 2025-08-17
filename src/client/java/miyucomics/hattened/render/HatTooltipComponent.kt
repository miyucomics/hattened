package miyucomics.hattened.render

import miyucomics.hattened.item.HatTooltipData
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipComponent

class HatTooltipComponent(val data: HatTooltipData) : TooltipComponent {
	override fun getHeight(textRenderer: TextRenderer) = if (data.abilities.isEmpty()) 0 else 52
	override fun getWidth(textRenderer: TextRenderer) = if (data.abilities.isEmpty()) 0 else data.abilities.size * 35

	override fun drawItems(textRenderer: TextRenderer, x: Int, y: Int, width: Int, height: Int, context: DrawContext) {
		data.abilities.forEachIndexed { index, ability ->
			context.matrices.pushMatrix()
			context.matrices.translate(x + index * 35 + 18f, y + 24f)
			CardRendererRegistry.render(context, textRenderer, ability, 2f, false)
			context.matrices.popMatrix()
		}
	}
}