package miyucomics.hattened.item

import miyucomics.hattened.HattenedHelper
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.attach.HatData
import miyucomics.hattened.inits.HattenedAttachments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class HatItem(settings: Settings) : Item(settings) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
//		val existing = HattenedHelper.getHatData(user)
//		if (existing.hasHat)
//			return ActionResult.FAIL
		if (!world.isClient) {
			val stack = user.getStackInHand(hand)
			stack.set(HattenedMain.ABILITY_COMPONENT, listOf(HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti"), HattenedMain.id("confetti")))
			user.setAttached(HattenedAttachments.HAT_DATA, HatData(true, 0, stack.getOrDefault(HattenedMain.ABILITY_COMPONENT, listOf())))
		}
		return ActionResult.SUCCESS
	}
}