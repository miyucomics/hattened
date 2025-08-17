package miyucomics.hattened.render

import dev.kosmx.playerAnim.api.PartKey
import dev.kosmx.playerAnim.api.TransformType
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hattened.misc.Poser
import miyucomics.hattened.structure.HattenedHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper

class HatPlayerModel(val player: PlayerEntity) : IAnimation {
	override fun setupAnim(tickDelta: Float) {}
	override fun isActive() = HattenedHelper.getHatData(player).hasHat

	override fun get3DTransform(modelKey: PartKey, type: TransformType, tickDelta: Float, original: Vec3f): Vec3f {
		if (type != TransformType.ROTATION)
			return original
		return Poser.rotateBody(HattenedHelper.getPose(player), modelKey, tickDelta, HattenedHelper.getHatData(player))?.scale(MathHelper.RADIANS_PER_DEGREE) ?: original
	}
}