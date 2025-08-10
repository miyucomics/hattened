package miyucomics.hattened.render

import dev.kosmx.playerAnim.api.PartKey
import dev.kosmx.playerAnim.api.TransformType
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.misc.ClientStorage
import miyucomics.hattened.misc.rotateBody
import miyucomics.hattened.structure.HatPose
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper

@Suppress("UnstableAPIUsage")
class HatPlayerModel(val player: PlayerEntity) : IAnimation {
	override fun setupAnim(tickDelta: Float) {}
	override fun isActive() = player.getAttached(HattenedAttachments.HAT_DATA)?.hasHat == true

	override fun get3DTransform(modelKey: PartKey, type: TransformType, tickDelta: Float, original: Vec3f): Vec3f {
		if (type != TransformType.ROTATION)
			return original
		val time = ClientStorage.ticks + tickDelta
		return (player.getAttached(HattenedAttachments.HAT_STATE_DATA)?.getAbility()?.getPose() ?: HatPose.OnHead).rotateBody(modelKey, time)?.scale(MathHelper.RADIANS_PER_DEGREE) ?: original
	}
}