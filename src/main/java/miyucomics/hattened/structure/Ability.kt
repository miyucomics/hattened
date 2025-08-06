package miyucomics.hattened.structure

import net.minecraft.entity.player.PlayerEntity

interface Ability {
	val name: String
	fun onSelected(player: PlayerEntity) {}
	fun onLeftClick(player: PlayerEntity) {}
	fun onRightClick(player: PlayerEntity) {}
	fun onTick(player: PlayerEntity) {}
}