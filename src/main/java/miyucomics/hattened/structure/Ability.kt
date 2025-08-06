package miyucomics.hattened.structure

import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerEntity
import org.joml.Matrix3x2fStack

interface Ability {
	val name: String
	fun onSelected(player: PlayerEntity) {}
	fun onLeftClick(player: PlayerEntity) {}
	fun onRightClick(player: PlayerEntity) {}
	fun onTick(player: PlayerEntity) {}
	fun renderIcon(context: DrawContext, matrices: Matrix3x2fStack, x: Int, y: Int, selected: Boolean, alpha: Float)
}