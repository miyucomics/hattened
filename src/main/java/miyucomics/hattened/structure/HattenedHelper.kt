package miyucomics.hattened.structure

import miyucomics.hattened.inits.HattenedAttachments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

@Suppress("UnstableApiUsage")
object HattenedHelper {
	@JvmStatic fun getHatData(player: PlayerEntity): HatData = player.getAttachedOrElse(HattenedAttachments.HAT_DATA, HatData.DEFAULT)
	@JvmStatic fun setHatData(player: ServerPlayerEntity, hat: HatData) = player.setAttached(HattenedAttachments.HAT_DATA, hat)

	@JvmStatic fun getPose(player: PlayerEntity): HatPose = player.getAttachedOrElse(HattenedAttachments.HAT_POSE, HatPose.OnHead)
	fun setPose(player: ServerPlayerEntity, pose: HatPose) = player.setAttached(HattenedAttachments.HAT_POSE, pose)
}