package miyucomics.hattened.structure

import net.minecraft.server.network.ServerPlayerEntity

interface Ability {
	val name: String
	fun onSelected(player: ServerPlayerEntity) {}
	fun onLeftClick(player: ServerPlayerEntity) {}
	fun onRightClick(player: ServerPlayerEntity) {}
	fun onTick(player: ServerPlayerEntity) {}
	fun getPose(): HatPose
}