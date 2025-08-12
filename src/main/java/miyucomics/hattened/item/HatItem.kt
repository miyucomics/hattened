package miyucomics.hattened.item

import miyucomics.hattened.HattenedHelper
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.attach.HatData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class HatItem(settings: Settings) : Item(settings) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
		val existing = HattenedHelper.getHatData(user)
		val existingStack = if (existing.hasHat) existing.toItemStack() else ItemStack.EMPTY
		if (!world.isClient) {
			val stack = user.getStackInHand(hand)
			stack.set(HattenedMain.ABILITY_COMPONENT, listOf(HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti")))
			HattenedHelper.setHatData(user, HatData(true, 0, stack.getOrDefault(HattenedMain.ABILITY_COMPONENT, listOf())))
			user.setStackInHand(hand, existingStack)
		}
		return ActionResult.SUCCESS
	}
}