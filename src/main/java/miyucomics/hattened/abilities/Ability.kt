package miyucomics.hattened.abilities

import com.mojang.serialization.Codec
import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.structure.HatPose
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.*

abstract class Ability(val type: AbilityType<*>, val uuid: UUID) {
	var leftClickHeld = false
	var rightClickHeld = false
	var removable = false

	abstract fun getTitle(): Text
	abstract fun getPose(): HatPose

	open fun tick(world: ServerWorld, player: ServerPlayerEntity) {
		if (leftClickHeld)
			onLeftClickHold(world, player)
		if (rightClickHeld)
			onRightClickHold(world, player)
	}

	open fun onLeftClick(world: ServerWorld, player: ServerPlayerEntity) { leftClickHeld = true }
	open fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) { rightClickHeld = true }
	open fun onLeftClickReleased(world: ServerWorld, player: ServerPlayerEntity) { leftClickHeld = false }
	open fun onRightClickReleased(world: ServerWorld, player: ServerPlayerEntity) { rightClickHeld = false }
	open fun onLeftClickHold(world: ServerWorld, player: ServerPlayerEntity) {}
	open fun onRightClickHold(world: ServerWorld, player: ServerPlayerEntity) {}

	fun switchOff(world: ServerWorld, player: ServerPlayerEntity) {
		onLeftClickReleased(world, player)
		onRightClickReleased(world, player)
	}

	companion object {
		val CODEC: Codec<Ability> = HattenedAbilities.ABILITY_REGISTRY.getCodec().dispatch("type", Ability::type, AbilityType<*>::codec)
	}
}