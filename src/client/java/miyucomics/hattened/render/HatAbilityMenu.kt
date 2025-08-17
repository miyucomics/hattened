package miyucomics.hattened.render

import miyucomics.hattened.misc.ClientStorage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import org.joml.Vector2f
import kotlin.math.abs
import kotlin.math.pow

object HatAbilityMenu {
	@JvmStatic
	fun render(context: DrawContext, progress: Float) {
		if (ClientStorage.cards.isEmpty())
			return
		val riseProgress = (progress / 0.5f).coerceAtMost(1f)
		val spreadProgress = ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f)

		val matrices = context.matrices
		matrices.pushMatrix()
		val client = MinecraftClient.getInstance()
		matrices.translate(client.window.scaledWidth / 2f, client.window.scaledHeight.toFloat())

		ClientStorage.orderedCards.forEach { (card, pos) ->
			val distance = 75 * 0.75f.pow(abs(pos) - 1)
			card.targetPosition = Vector2f(spreadProgress * pos * distance, -140f * riseProgress + 70)
			card.targetAngle = spreadProgress * pos * 10f
			card.targetScale = 0.75.pow(abs(pos)).toFloat()

			card.tick()
			card.render(context)
		}

		matrices.popMatrix()
	}
}