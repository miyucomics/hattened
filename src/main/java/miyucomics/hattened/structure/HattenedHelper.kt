package miyucomics.hattened.structure

import miyucomics.hattened.inits.HattenedAttachments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*
import kotlin.math.max

@Suppress("UnstableApiUsage")
object HattenedHelper {
	@JvmStatic fun getHatData(player: PlayerEntity): HatData = player.getAttachedOrElse(HattenedAttachments.HAT_DATA, HatData.DEFAULT)
	@JvmStatic fun setHatData(player: ServerPlayerEntity, hat: HatData) = player.setAttached(HattenedAttachments.HAT_DATA, hat)

	@JvmStatic fun getPose(player: PlayerEntity): HatPose = player.getAttachedOrElse(HattenedAttachments.HAT_POSE, HatPose.OnHead)
	fun setPose(player: ServerPlayerEntity, pose: HatPose) = player.setAttached(HattenedAttachments.HAT_POSE, pose)

	@JvmStatic
	fun updateHat(player: ServerPlayerEntity, inputQueue: Queue<UserInput>, proposedAdditions: Queue<ItemStack>, setCooldown: Optional<Int>) {
		var hat = this.getHatData(player)
		while (inputQueue.isNotEmpty())
			hat = hat.transition(inputQueue.poll())

		hat = hat.copy(cooldown = max(0, setCooldown.orElse(hat.cooldown - 1)))
		hat.tick(player)

		val newStorage = hat.storage.mapNotNull { if (it.mutated) it.replacement else it }.toMutableList()
		while (proposedAdditions.isNotEmpty())
			newStorage.add(ServerCard(proposedAdditions.poll()))
		this.setHatData(player, hat.copy(storage = newStorage))
	}
}