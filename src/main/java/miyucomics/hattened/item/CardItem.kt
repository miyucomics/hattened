package miyucomics.hattened.item

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.ConfettiAbility
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object CardItem : Item(Settings().maxCount(1).component(HattenedMain.ABILITY_COMPONENT, ConfettiAbility()).registryKey(HattenedMain.CARD_ITEM_KEY)) {
	override fun getName(stack: ItemStack): Text? = stack.get(HattenedMain.ABILITY_COMPONENT)?.getText() ?: super.getName(stack)
}