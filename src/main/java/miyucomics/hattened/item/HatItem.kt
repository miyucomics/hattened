package miyucomics.hattened.item

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.structure.HatData
import miyucomics.hattened.structure.HattenedHelper
import miyucomics.hattened.structure.ServerCard
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.TooltipDisplayComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipData
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.ClickType
import net.minecraft.util.Hand
import net.minecraft.world.World
import java.util.*

object HatItem : Item(Settings().maxCount(1).component(HattenedMain.HAT_STORAGE_COMPONENT, listOf()).registryKey(HattenedMain.HAT_ITEM_KEY)) {
	override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
		val hat = HattenedHelper.getHatData(user)
		val oldHat = if (hat.hasHat) hat.toItemStack() else ItemStack.EMPTY
		if (!world.isClient) {
			val newHat = user.getStackInHand(hand)
			HattenedHelper.setHatData(user as ServerPlayerEntity, HatData(true, newHat.getOrDefault(HattenedMain.HAT_STORAGE_COMPONENT, listOf())))
			user.setStackInHand(hand, oldHat)
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

		if (clickType == ClickType.RIGHT && clickedStack.isEmpty && hat.getOrDefault(HattenedMain.HAT_STORAGE_COMPONENT, listOf()).isNotEmpty()) {
			val extracted = removeItem(hat)
			if (extracted != null) {
				slot.stack = extracted
				return true
			}
		}

		return false
	}

	override fun onClicked(hat: ItemStack, clickedStack: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity, cursorStackReference: StackReference): Boolean {
		val existing = hat.getOrDefault(HattenedMain.HAT_STORAGE_COMPONENT, emptyList())

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

	override fun getTooltipData(stack: ItemStack): Optional<TooltipData> {
		val displayHandler = stack.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT)
		if (displayHandler.shouldDisplay(HattenedMain.HAT_STORAGE_COMPONENT))
			return Optional.ofNullable(stack.get(HattenedMain.HAT_STORAGE_COMPONENT)).map(::HatTooltipData)
		return Optional.empty()
	}

	fun insertItem(hat: ItemStack, stack: ItemStack) {
		hat.set(HattenedMain.HAT_STORAGE_COMPONENT, hat.getOrDefault(HattenedMain.HAT_STORAGE_COMPONENT, emptyList()).plus(ServerCard(stack)))
	}

	fun removeItem(hat: ItemStack): ItemStack? {
		val storage = hat.getOrDefault(HattenedMain.HAT_STORAGE_COMPONENT, emptyList())
		if (storage.isEmpty())
			return null

		val new = storage.toMutableList()
		val extracted = new.removeFirst()
		hat.set(HattenedMain.HAT_STORAGE_COMPONENT, new)
		return extracted.stack
	}
}

data class HatTooltipData(val storage: List<ServerCard>) : TooltipData