package miyucomics.hattened.render

import dev.kosmx.playerAnim.api.PartKey
import dev.kosmx.playerAnim.api.TransformType
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.misc.ClientStorage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.cos
import kotlin.math.sin

class HatPlayerModel(val player: PlayerEntity) : IAnimation {
	override fun setupAnim(tickDelta: Float) {}
	override fun isActive() = player.getAttached(HattenedAttachments.HAT_DATA)?.isUsingHat() == true

	override fun get3DTransform(modelKey: PartKey, type: TransformType, tickDelta: Float, original: Vec3f): Vec3f {
		val time = ClientStorage.ticks + tickDelta

		if (modelKey == PartKey.LEFT_ARM && type == TransformType.ROTATION)
			return Vec3f(-50f, 45f, 0f).scale(MathHelper.RADIANS_PER_DEGREE)

		if (modelKey == PartKey.RIGHT_ARM && type == TransformType.ROTATION) {
			val pitch = sin(time / 3f) * 2f
			val yaw = cos(time / 2f) * 2f
			return Vec3f(-50f + pitch, -25f + yaw, 0f).scale(MathHelper.RADIANS_PER_DEGREE)
		}

		return original
	}
}