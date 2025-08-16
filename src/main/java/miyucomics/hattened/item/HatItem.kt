package miyucomics.hattened.item

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.ItemStackAbility
import miyucomics.hattened.abilities.VacuumAbility
import miyucomics.hattened.structure.HatData
import miyucomics.hattened.structure.HattenedHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import java.util.*

class HatItem(settings: Settings) : Item(settings) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
		val existing = HattenedHelper.getHatData(user)
		val existingStack = if (existing.hasHat) existing.toItemStack() else ItemStack.EMPTY
		if (!world.isClient) {
			val stack = user.getStackInHand(hand)
			stack.set(HattenedMain.ABILITY_COMPONENT, listOf(
				VacuumAbility(UUID.randomUUID()),
				ItemStackAbility(ItemStack(Items.APPLE)),
				ItemStackAbility(ItemStack(Items.TRADER_LLAMA_SPAWN_EGG)),
				ItemStackAbility(ItemStack(Items.DAMAGED_ANVIL)),
			))
			HattenedHelper.setHatData(user as ServerPlayerEntity, HatData(true, stack.getOrDefault(HattenedMain.ABILITY_COMPONENT, listOf())))
			user.setStackInHand(hand, existingStack)
		}
		return ActionResult.SUCCESS
	}
}