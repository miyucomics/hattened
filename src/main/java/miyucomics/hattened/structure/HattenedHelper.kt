package miyucomics.hattened.structure

import miyucomics.hattened.inits.HattenedAttachments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*
import kotlin.math.min

@Suppress("UnstableApiUsage")
object HattenedHelper {
	@JvmStatic fun getHatData(player: PlayerEntity): HatData = player.getAttachedOrElse(HattenedAttachments.HAT_DATA, HatData.DEFAULT)
	@JvmStatic fun setHatData(player: ServerPlayerEntity, hat: HatData) = player.setAttached(HattenedAttachments.HAT_DATA, hat)

	@JvmStatic fun getPose(player: PlayerEntity): HatPose = player.getAttachedOrElse(HattenedAttachments.HAT_POSE, HatPose.OnHead)
	fun setPose(player: ServerPlayerEntity, pose: HatPose) = player.setAttached(HattenedAttachments.HAT_POSE, pose)

	fun insertStack(storage: List<ServerCard>, stack: ItemStack): List<ServerCard> {
		for (card in storage) {
			if (ItemStack.areItemsAndComponentsEqual(card.stack, stack)) {
				val transferAmount = min(card.stack.maxCount - card.stack.count, stack.count)
				stack.split(transferAmount)
				card.stack.increment(transferAmount)
				card.markDirty()
				if (stack.isEmpty)
					return storage.toList()
			}
		}

		return storage.plus(ServerCard(stack))
	}

	@JvmStatic
	fun updateHat(player: ServerPlayerEntity, inputQueue: Queue<UserInput>, proposedAdditions: Queue<ItemStack>) {
		var hat = this.getHatData(player)
		while (inputQueue.isNotEmpty())
			hat = hat.updateInternalState(inputQueue.poll())

		hat.tick(player)
		val firstCard = hat.storage.firstOrNull()
		if (firstCard != null && firstCard.mutated && firstCard.replacement == null)
			hat = hat.copy(isThrowingItems = false)

		var newStorage = hat.storage.mapNotNull { if (it.mutated) it.replacement else it }
		while (proposedAdditions.isNotEmpty())
			newStorage = insertStack(newStorage, proposedAdditions.poll())
		this.setHatData(player, hat.copy(storage = newStorage))
	}
}