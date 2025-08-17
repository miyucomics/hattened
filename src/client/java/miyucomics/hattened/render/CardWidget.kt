package miyucomics.hattened.render

import miyucomics.hattened.structure.ServerCard
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import org.joml.Vector2f

class CardWidget(var index: Int, var card: ServerCard) {
	private var angle = 0f
	private var position = Vector2f(0f, 75f)
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
		val textRenderer = MinecraftClient.getInstance().textRenderer
		context.matrices.pushMatrix()
		context.matrices.translate(this.position.x, this.position.y)
		context.matrices.rotate(this.angle / 180f * MathHelper.PI)
		context.matrices.scale(this.scale)
		CardRenderer.render(context, textRenderer, this.card, 4f, true)
		context.matrices.popMatrix()
	}

	fun canRemove() = this.scale <= 0.05f && this.removing

	companion object {
		private const val LERP_SPEED = 0.15f
	}
}