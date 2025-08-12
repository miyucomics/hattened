package miyucomics.hattened.structure

import net.minecraft.server.network.ServerPlayerEntity

abstract class BaseAbility(override val name: String) : Ability {
	var cooldownTicks = 0

	override fun onTick(player: ServerPlayerEntity) {
		if (cooldownTicks > 0)
			cooldownTicks--
	}

	override fun getPose() = HatPose.SearchingHat
}