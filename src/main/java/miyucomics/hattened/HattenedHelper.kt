package miyucomics.hattened

import miyucomics.hattened.attach.HatDataAttachment
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.structure.HatPose
import net.minecraft.entity.player.PlayerEntity

@Suppress("UnstableApiUsage")
object HattenedHelper {
	@JvmStatic fun getHatData(player: PlayerEntity): HatDataAttachment = player.getAttachedOrElse(HattenedAttachments.HAT_DATA, HatDataAttachment.DEFAULT)
	fun setHatData(player: PlayerEntity, hat: HatDataAttachment) {
		player.setAttached(HattenedAttachments.HAT_DATA, hat)
	}

	@JvmStatic fun getPose(player: PlayerEntity): HatPose = player.getAttachedOrElse(HattenedAttachments.HAT_POSE, HatPose.OnHead)
	fun setPose(player: PlayerEntity, pose: HatPose) = player.setAttached(HattenedAttachments.HAT_POSE, pose)
}