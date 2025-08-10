package miyucomics.hattened.abilities

import miyucomics.hattened.structure.BaseAbility
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class VacuumAbility(val stack: ItemStack) : BaseAbility("spawn_item") {
	override fun onLeftClick(player: PlayerEntity) {
		if (!canUse())
			return
	}

	override fun onRightClick(player: PlayerEntity) {
		player.world.spawnEntity(ItemEntity(player.world, player.x, player.y, player.z, stack.copy()))
	}
}