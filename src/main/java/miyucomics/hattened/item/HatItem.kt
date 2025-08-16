package miyucomics.hattened.item

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.ConfettiAbility
import miyucomics.hattened.abilities.ItemStackAbility
import miyucomics.hattened.structure.HatData
import miyucomics.hattened.structure.HattenedHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.ClickType
import net.minecraft.util.Hand
import net.minecraft.world.World
import java.util.UUID

object HatItem : Item(Settings().maxCount(1).component(HattenedMain.ABILITIES_COMPONENT, listOf()).registryKey(HattenedMain.HAT_ITEM_KEY)) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
		val existing = HattenedHelper.getHatData(user)
		val existingStack = if (existing.hasHat) existing.toItemStack() else ItemStack.EMPTY
		if (!world.isClient) {
			val stack = user.getStackInHand(hand)
			HattenedHelper.setHatData(user as ServerPlayerEntity, HatData(true, stack.getOrDefault(HattenedMain.ABILITIES_COMPONENT, listOf())))
			user.setStackInHand(hand, existingStack)
		}
		return ActionResult.SUCCESS
	}

	override fun onStackClicked(hat: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity): Boolean {
		val clickedStack = slot.stack

		if (clickType == ClickType.LEFT && !clickedStack.isEmpty) {
			insertItem(hat, clickedStack)
			slot.stack = ItemStack.EMPTY
			return true
		}

		if (clickType == ClickType.RIGHT && clickedStack.isEmpty && hat.getOrDefault(HattenedMain.ABILITIES_COMPONENT, listOf()).isNotEmpty()) {
			val extracted = removeItem(hat)
			if (extracted != null) {
				slot.stack = extracted
				return true
			}
		}

		return false
	}

	override fun onClicked(hat: ItemStack, clickedStack: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity, cursorStackReference: StackReference): Boolean {
		val existing = hat.getOrDefault(HattenedMain.ABILITIES_COMPONENT, emptyList())

		if (clickType == ClickType.LEFT && !clickedStack.isEmpty) {
			insertItem(hat, clickedStack)
			cursorStackReference.set(ItemStack.EMPTY)
			return true
		}

		if (clickType == ClickType.RIGHT && clickedStack.isEmpty && existing.isNotEmpty()) {
			val extracted = removeItem(hat)
			if (extracted != null) {
				cursorStackReference.set(extracted)
				return true
			}
		}

		return false
	}

	fun insertItem(hat: ItemStack, stack: ItemStack) {
		val newAbilities = hat.getOrDefault(HattenedMain.ABILITIES_COMPONENT, emptyList()).toMutableList()
		if (stack.isOf(HattenedMain.CARD_ITEM))
			newAbilities.add(stack.getOrDefault(HattenedMain.ABILITY_COMPONENT, ConfettiAbility()).also { it.uuid = UUID.randomUUID() })
		else
			newAbilities.add(ItemStackAbility(stack))
		hat.set(HattenedMain.ABILITIES_COMPONENT, newAbilities)
	}

	fun removeItem(hat: ItemStack): ItemStack? {
		val existingAbilities = hat.getOrDefault(HattenedMain.ABILITIES_COMPONENT, emptyList())
		if (existingAbilities.isEmpty())
			return null

		val newAbilities = existingAbilities.toMutableList()
		val extracted = newAbilities.removeFirst()
		hat.set(HattenedMain.ABILITIES_COMPONENT, newAbilities)
		return ItemStack(HattenedMain.CARD_ITEM).apply { set(HattenedMain.ABILITY_COMPONENT, extracted) }
	}
}