package miyucomics.hattened.structure

import net.minecraft.item.ItemStack

@Suppress("FunctionName")
interface ServerPlayerEntityMinterface {
	fun `hattened$queueUserInput`(input: UserInput)
	fun `hattened$proposeItemStack`(stack: ItemStack)
	fun `hattened$setCooldown`(cooldown: Int)
}