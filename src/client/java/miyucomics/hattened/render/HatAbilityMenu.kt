package miyucomics.hattened.render

import miyucomics.hattened.misc.ClientStorage
import miyucomics.hattened.render.Card.Companion.CARD_HEIGHT
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
		val cardCount = ClientStorage.cards.size
		val riseProgress = (progress / 0.5f).coerceAtMost(1f)
		val spreadProgress = ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f)

		val matrices = context.matrices
		matrices.pushMatrix()
		val client = MinecraftClient.getInstance()
		matrices.translate(client.window.scaledWidth / 2f, client.window.scaledHeight.toFloat())

		ClientStorage.cards.values.forEach { card ->
			var relativeIndex = card.index
			if (relativeIndex >= cardCount / 2f)
				relativeIndex -= cardCount
			card.targetPosition = Vector2f(spreadProgress * relativeIndex * 70, CARD_HEIGHT.toFloat() + -150f * riseProgress)
			card.targetAngle = spreadProgress * relativeIndex * 10f
			card.targetScale = 0.75.pow(abs(relativeIndex)).toFloat()

			card.tick()
			card.render(context)
		}

		matrices.popMatrix()
	}
}