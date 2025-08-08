package miyucomics.hattened.render

import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.misc.ClientStorage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

object HatAbilityMenu {
	private const val FAN_RADIUS = 200f
	private const val FAN_ANGLE = 90f
	private const val STACK_RISE_HEIGHT = 10f
	private const val CARD_WIDTH = 40
	private const val CARD_HEIGHT = 60
	private const val TWEEN_SPEED = 0.15f

	private var animatedCards: MutableList<AnimatedCard> = mutableListOf()

	fun initializeCards() {
		animatedCards = HattenedAbilities.ABILITY_REGISTRY.keys.map { key -> AnimatedCard(key.value) }.toMutableList()
	}

	@JvmStatic
	fun render(context: DrawContext, progress: Float) {
		if (animatedCards.isEmpty())
			return

		val client = MinecraftClient.getInstance()
		val screenWidth = client.window.scaledWidth
		val screenHeight = client.window.scaledHeight

		val cardCount = animatedCards.size
		val riseProgress = (progress / 0.5f).coerceAtMost(1f)
		val spreadProgress = ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f)

		val fanOriginX = screenWidth / 2f
		val fanOriginY = screenHeight.toFloat()
		val anglePerCardInFan = FAN_ANGLE / max(1f, cardCount.toFloat() - 1f)

		for (i in 0..<cardCount) {
			val card = animatedCards[i]
			card.updateTarget(ClientStorage.abilityIndex, cardCount, riseProgress, spreadProgress, anglePerCardInFan)
			card.tick(TWEEN_SPEED)
			card.render(context, fanOriginX, fanOriginY, i == ClientStorage.abilityIndex)
		}
	}

	private class AnimatedCard(private val abilityId: Identifier) {
		private var currentAngle = 0f
		private var currentYOffset = 0f

		private var targetAngle = 0f
		private var targetYOffset = 0f

		fun updateTarget(selectedAbilityIndex: Int, totalCards: Int, riseProgress: Float, spreadProgress: Float, anglePerCardInFan: Float) {
			this.targetAngle = 0f
			this.targetYOffset = STACK_RISE_HEIGHT * riseProgress

			if (spreadProgress > 0) {
				val rawOffset = this.abilityIndex - selectedAbilityIndex
				var effectiveOffset = rawOffset

				if (totalCards > 1) {
					if (rawOffset > totalCards / 2) {
						effectiveOffset -= totalCards
					} else if (rawOffset < -totalCards / 2) {
						effectiveOffset += totalCards
					}
				}

				val targetCardAngleDegrees = effectiveOffset * anglePerCardInFan
				this.targetAngle = targetCardAngleDegrees * spreadProgress
				this.targetYOffset = STACK_RISE_HEIGHT
			}
		}

		fun tick(speed: Float) {
			this.currentYOffset = MathHelper.lerp(speed, this.currentYOffset, this.targetYOffset)
			val angleDiff = this.targetAngle - this.currentAngle
			if (angleDiff > 180) {
				this.currentAngle += 360f
			} else if (angleDiff < -180) {
				this.currentAngle -= 360f
			}
			this.currentAngle = MathHelper.lerp(speed, this.currentAngle, this.targetAngle)
			this.currentAngle = (this.currentAngle % 360 + 360) % 360
		}

		fun render(context: DrawContext, fanOriginX: Float, fanOriginY: Float, isSelected: Boolean) {
			val cardOffsetX = (FAN_RADIUS * sin(Math.toRadians(currentAngle.toDouble()))).toFloat()
			val cardOffsetY = -(FAN_RADIUS * cos(Math.toRadians(currentAngle.toDouble()))).toFloat()
			val finalCardX = fanOriginX + cardOffsetX
			val finalCardY = fanOriginY + cardOffsetY - currentYOffset
			context.matrices.pushMatrix()
			context.matrices.translate(finalCardX, finalCardY)
			context.matrices.rotate(currentAngle / 180f * MathHelper.PI)
			context.matrices.translate(-CARD_WIDTH / 2f, -CARD_HEIGHT / 2f)
			val cardColor = if (isSelected) -0x55222300 else -0x55010000
			context.fill(0, 0, CARD_WIDTH, CARD_HEIGHT, cardColor)
			context.drawText(MinecraftClient.getInstance().textRenderer, abilityId.toString(), 0, 0, 0xff00ff00.toInt(), true)
			context.matrices.popMatrix()
		}

		val abilityIndex: Int
			get() = HattenedAbilities.ABILITY_REGISTRY.keys.indexOfFirst { it.value == abilityId }
	}
}