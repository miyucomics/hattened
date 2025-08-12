package miyucomics.hattened.render

import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.misc.ClientStorage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import org.joml.Vector2f
import kotlin.math.abs

object HatAbilityMenu {
	private var animatedCards: MutableList<Card> = mutableListOf()

	fun initializeCards() {
		animatedCards = HattenedAbilities.ABILITY_REGISTRY.keys.map { key -> Card(key.value) }.toMutableList()
	}

	@JvmStatic
	fun render(context: DrawContext, progress: Float) {
		if (animatedCards.isEmpty())
			return
		val cardCount = animatedCards.size
		val riseProgress = (progress / 0.5f).coerceAtMost(1f)
		val spreadProgress = ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f)

		context.drawText(MinecraftClient.getInstance().textRenderer, ClientStorage.abilityIndex.toString(), 0, 0, 0xff000000.toInt(), true)

		val matrices = context.matrices
		matrices.pushMatrix()
		val client = MinecraftClient.getInstance()
		matrices.translate(client.window.scaledWidth / 2f, client.window.scaledHeight.toFloat())

		for (i in 0..<cardCount) {
			val card = animatedCards[i]
			var relativeIndex = (i - ClientStorage.abilityIndex).mod(cardCount)
			if (relativeIndex >= cardCount / 2f)
				relativeIndex -= cardCount
			card.targetPosition = Vector2f(spreadProgress * relativeIndex * 70, -50f * riseProgress)
			card.targetAngle = spreadProgress * relativeIndex * 10f
			card.targetScale = 1f / (abs(relativeIndex) + 1)

			card.tick()
			card.render(context, i)
		}

		matrices.popMatrix()
	}
}