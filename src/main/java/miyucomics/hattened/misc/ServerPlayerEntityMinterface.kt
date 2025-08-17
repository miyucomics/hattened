package miyucomics.hattened.misc

import net.minecraft.item.ItemStack

interface ServerPlayerEntityMinterface {
	fun queueUserInput(input: UserInput)
	fun proposeItemStack(stack: ItemStack)
}