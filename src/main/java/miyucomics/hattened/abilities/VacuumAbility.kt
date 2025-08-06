package miyucomics.hattened.abilities

import miyucomics.hattened.structure.BaseAbility
import net.minecraft.entity.player.PlayerEntity

class VacuumAbility : BaseAbility("vacuum") {
	override fun onLeftClick(player: PlayerEntity) {
		if (!canUse())
			return
	}

	override fun onRightClick(player: PlayerEntity) {
		if (!canUse())
			return

		player.addVelocity(0.0, 10.0, 0.0)
	}
}