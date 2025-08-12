package miyucomics.hattened

import miyucomics.hattened.attach.HatData
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.structure.HatPose
import net.minecraft.entity.player.PlayerEntity

@Suppress("UnstableApiUsage")
object HattenedHelper {
	@JvmStatic fun getHatData(player: PlayerEntity): HatData = player.getAttachedOrElse(HattenedAttachments.HAT_DATA, HatData.DEFAULT)
	@JvmStatic fun setHatData(player: PlayerEntity, hat: HatData) {
		player.setAttached(HattenedAttachments.HAT_DATA, hat)
	}
}