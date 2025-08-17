package miyucomics.hattened.render

import miyucomics.hattened.misc.HatTooltipData
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipComponent

class HatTooltipComponent(val data: HatTooltipData) : TooltipComponent {
	override fun getHeight(textRenderer: TextRenderer) = if (data.storage.isEmpty()) 0 else 52
	override fun getWidth(textRenderer: TextRenderer) = if (data.storage.isEmpty()) 0 else data.storage.size * 35

	override fun drawItems(textRenderer: TextRenderer, x: Int, y: Int, width: Int, height: Int, context: DrawContext) {
		data.storage.forEachIndexed { index, card ->
			context.matrices.pushMatrix()
			context.matrices.translate(x + index * 35 + 18f, y + 24f)
			CardRenderer.render(context, textRenderer, card, 2f, false)
			context.matrices.popMatrix()
		}
	}
}