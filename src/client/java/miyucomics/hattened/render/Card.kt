package miyucomics.hattened.render

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.Ability
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import org.joml.Vector2f

class Card(var index: Int, var ability: Ability) {
	private var angle = 0f
	private var position = Vector2f(0f, CARD_HEIGHT.toFloat())
	private var scale = 1f
	var removing = false
	var targetAngle = 0f
	var targetPosition = Vector2f()
	var targetScale = 1f

	fun tick() {
		this.position = this.position.lerp(this.targetPosition, LERP_SPEED)
		this.angle = MathHelper.lerp(LERP_SPEED, this.angle, this.targetAngle)
		if (this.removing)
			this.targetScale = 0f
		this.scale = MathHelper.lerp(LERP_SPEED, this.scale, this.targetScale)
	}

	fun render(context: DrawContext) {
		context.matrices.pushMatrix()
		context.matrices.translate(this.position.x, this.position.y)
		context.matrices.rotate(this.angle / 180f * MathHelper.PI)
		context.matrices.scale(this.scale)
		context.matrices.translate(-CARD_WIDTH / 2f, -CARD_HEIGHT / 2f)
		context.drawTexturedQuad(HattenedMain.id("textures/cards/base.png"), 0, 0, CARD_WIDTH, CARD_HEIGHT, 0f, 1f, 0f, 1f)
		context.matrices.translate(CARD_WIDTH / 2f, CARD_HEIGHT / 2f)
		val text = this.ability.getText()
		context.drawText(MinecraftClient.getInstance().textRenderer, text, MinecraftClient.getInstance().textRenderer.getWidth(text) / -2, -CARD_HEIGHT / 2 - 14, 0xffffffff.toInt(), true)
		context.matrices.pushMatrix()
		CardRendererRegistry.render(context, this.ability)
		context.matrices.popMatrix()
		context.matrices.popMatrix()
	}

	fun canRemove() = this.scale <= 0.05f && this.removing

	companion object {
		private const val LERP_SPEED = 0.15f
		private const val CARD_WIDTH = 60
		const val CARD_HEIGHT = 90
	}
}