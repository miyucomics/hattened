package miyucomics.hattened.item

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.structure.HatDataAttachment
import miyucomics.hattened.structure.HattenedHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class HatItem(settings: Settings) : Item(settings) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
		val existing = HattenedHelper.getHatData(user)
		val existingStack = if (existing.hasHat) existing.toItemStack() else ItemStack.EMPTY
		if (!world.isClient) {
			val stack = user.getStackInHand(hand)
			HattenedHelper.setHatData(user as ServerPlayerEntity, HatDataAttachment(true, 0, stack.getOrDefault(HattenedMain.ABILITY_COMPONENT, listOf())))
			user.setStackInHand(hand, existingStack)
		}
		return ActionResult.SUCCESS
	}
}