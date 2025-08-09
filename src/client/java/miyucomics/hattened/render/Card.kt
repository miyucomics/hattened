package miyucomics.hattened.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import org.joml.Vector2f

class Card(val abilityId: Identifier) {
	private var currentAngle = 0f
	private var currentPosition = Vector2f()
	var targetAngle = 0f
	var targetPosition = Vector2f()

	fun tick() {
		this.currentPosition = this.currentPosition.lerp(targetPosition, LERP_SPEED)
		this.currentAngle = MathHelper.lerp(LERP_SPEED, this.currentAngle, this.targetAngle)
	}

	fun render(context: DrawContext) {
		context.matrices.pushMatrix()
		context.matrices.translate(this.currentPosition.x, this.currentPosition.y)
		context.matrices.rotate(this.currentAngle / 180f * MathHelper.PI)
		context.matrices.translate(-CARD_WIDTH / 2f, 0f)
		context.fill(0, 0, CARD_WIDTH, CARD_HEIGHT, 0xaaffffff.toInt())
		context.drawText(MinecraftClient.getInstance().textRenderer, abilityId.toString(), 0, 0, 0xffffffff.toInt(), true)
		context.matrices.popMatrix()
	}

	companion object {
		private const val LERP_SPEED = 0.15f
		private const val CARD_WIDTH = 60
		private const val CARD_HEIGHT = 90
	}
}