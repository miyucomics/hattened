package miyucomics.hattened.structure

import net.minecraft.item.ItemStack

interface ServerPlayerEntityMinterface {
	fun queueUserInput(input: UserInput)
	fun proposeItemStack(stack: ItemStack)
	fun setCooldown(cooldown: Int)
}