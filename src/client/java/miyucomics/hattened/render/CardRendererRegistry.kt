package miyucomics.hattened.render

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.structure.ServerCard
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext

object CardRenderer {
	fun render(context: DrawContext, textRenderer: TextRenderer, card: ServerCard, scale: Float, drawText: Boolean) {
		if (drawText) {
			val text = card.stack.name
			context.drawText(textRenderer, text, -textRenderer.getWidth(text) / 2, -14 * scale.toInt() - 5, 0xffffffff.toInt(), true)
		}

		context.matrices.pushMatrix()
		context.matrices.scale(scale)
		context.drawTexturedQuad(HattenedMain.id("textures/cards/base.png"), -8, -12, 8, 12, 0f, 1f, 0f, 1f)
		context.matrices.pushMatrix()
		context.matrices.scale(0.6f)
		context.matrices.translate(-8f, -8f)
		context.drawItem(card.stack, 0, 0)
		context.drawStackOverlay(MinecraftClient.getInstance().textRenderer, card.stack, 0, 0)
		context.matrices.popMatrix()
		context.matrices.popMatrix()
	}
}