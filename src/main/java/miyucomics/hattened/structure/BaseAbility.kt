package miyucomics.hattened.structure

import net.minecraft.entity.player.PlayerEntity

abstract class BaseAbility(override val name: String) : Ability {
	var cooldownTicks = 0

	override fun onTick(player: PlayerEntity) {
		if (cooldownTicks > 0)
			cooldownTicks--
	}

	fun canUse(): Boolean = cooldownTicks == 0
	fun useCooldown(ticks: Int) { cooldownTicks = ticks }
	override fun getPose() = HatPose.SearchingHat
}