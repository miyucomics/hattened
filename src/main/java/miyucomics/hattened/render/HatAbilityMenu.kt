package miyucomics.hattened.render

import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.misc.ClientStorage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import kotlin.math.cos
import kotlin.math.sin

object HatAbilityMenu {
	const val FAN_RADIUS = 200f
	const val FAN_ANGLE = 90f

	@JvmStatic
	fun render(context: DrawContext, progress: Float) {
		val client = MinecraftClient.getInstance()
		val width = client.window.scaledWidth
		val height = client.window.scaledHeight
		val riseProgress = (progress.coerceAtMost(0.5f) / 0.5f)
		val spreadProgress = ((progress - 0.5f) * 2f).coerceIn(0f, 1f)

		val abilities = HattenedAbilities.ABILITY_REGISTRY.ids.toList()

		val centerX = width / 2
		val baseY = height
		val cardCount = abilities.size

		for (i in 0 until cardCount) {
			val offset = i - ClientStorage.animatedAbilityIndex

			val angle = offset * (FAN_ANGLE / cardCount) * spreadProgress
			val radians = Math.toRadians(angle.toDouble())
			val dx = (FAN_RADIUS * sin(radians)).toFloat()
			val dy = -(FAN_RADIUS * cos(radians)).toFloat()

			val x = centerX + dx
			val y = baseY + dy - riseProgress * 40f

			val matrices = context.matrices
			matrices.pushMatrix()
			matrices.translate(x, y)
			matrices.rotate(angle / 180 * MathHelper.PI)
			val ability = HattenedAbilities.ABILITY_REGISTRY.get(abilities[i])
			ability?.renderIcon(context, matrices, 0, 0, offset == 0f, spreadProgress)
			matrices.popMatrix()
		}
	}
}