package miyucomics.hattened.structure

import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerEntity
import org.joml.Matrix3x2fStack

abstract class BaseAbility(override val name: String) : Ability {
	var cooldownTicks = 0

	override fun onTick(player: PlayerEntity) {
		if (cooldownTicks > 0)
			cooldownTicks--
	}

	fun canUse(): Boolean = cooldownTicks == 0

	fun useCooldown(ticks: Int) {
		cooldownTicks = ticks
	}

	override fun renderIcon(
		context: DrawContext,
		matrices: Matrix3x2fStack,
		x: Int,
		y: Int,
		selected: Boolean,
		alpha: Float
	) {
		context.fill(x, y, x + 40, y + 60, 0xAAFF0000.toInt())
	}
}