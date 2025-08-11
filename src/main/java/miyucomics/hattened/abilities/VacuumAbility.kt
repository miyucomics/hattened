package miyucomics.hattened.abilities

import miyucomics.hattened.structure.BaseAbility
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

class VacuumAbility(val stack: ItemStack) : BaseAbility("spawn_item") {
	override fun onLeftClick(player: ServerPlayerEntity) {
		if (!canUse())
			return
	}

	override fun onRightClick(player: ServerPlayerEntity) {
		player.world.spawnEntity(ItemEntity(player.world, player.x, player.y, player.z, stack.copy()))
	}
}