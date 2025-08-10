package miyucomics.hattened.render

import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.misc.ClientStorage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import org.joml.Vector2f

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

		val matrices = context.matrices
		matrices.pushMatrix()
		val client = MinecraftClient.getInstance()
		matrices.translate(client.window.scaledWidth / 2f, client.window.scaledHeight.toFloat())

		for (i in 0..<cardCount) {
			val card = animatedCards[i]
			val relativeIndex = (i - ClientStorage.abilityIndex).mod(cardCount) - cardCount / 2
			card.targetPosition = Vector2f(spreadProgress * relativeIndex * 100, -100f * riseProgress)
			card.targetAngle = 0f

			card.tick()
			card.render(context)
		}

		matrices.popMatrix()
	}
}