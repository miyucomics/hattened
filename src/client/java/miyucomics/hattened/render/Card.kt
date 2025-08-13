package miyucomics.hattened.render

import miyucomics.hattened.structure.Ability
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import org.joml.Vector2f

class Card(val ability: Ability) {
	private var angle = 0f
	private var position = Vector2f(0f, CARD_HEIGHT.toFloat())
	private var scale = 1f
	var targetAngle = 0f
	var targetPosition = Vector2f()
	var targetScale = 1f

	fun tick() {
		this.position = this.position.lerp(targetPosition, LERP_SPEED)
		this.angle = MathHelper.lerp(LERP_SPEED, this.angle, this.targetAngle)
		this.scale = MathHelper.lerp(LERP_SPEED, this.scale, this.targetScale)
	}

	fun render(context: DrawContext) {
		context.matrices.pushMatrix()
		context.matrices.translate(this.position.x, this.position.y)
		context.matrices.rotate(this.angle / 180f * MathHelper.PI)
		context.matrices.scale(this.scale)
		val text = ability.getTitle()
		context.drawText(MinecraftClient.getInstance().textRenderer, text, MinecraftClient.getInstance().textRenderer.getWidth(text) / -2, -CARD_HEIGHT / 2 - 14, 0xffffffff.toInt(), true)
		context.matrices.translate(-CARD_WIDTH / 2f, -CARD_HEIGHT / 2f)
		context.fill(0, 0, CARD_WIDTH, CARD_HEIGHT, 0xff000022.toInt())
		context.matrices.popMatrix()
	}

	companion object {
		private const val LERP_SPEED = 0.15f
		private const val CARD_WIDTH = 60
		const val CARD_HEIGHT = 90
	}
}