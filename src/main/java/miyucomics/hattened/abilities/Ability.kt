package miyucomics.hattened.abilities

import com.mojang.serialization.Codec
import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.structure.HatData
import miyucomics.hattened.structure.HatPose
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.*

abstract class Ability(val type: AbilityType<*>, val uuid: UUID) {
	var mutated = false
	var replacement: Ability? = null

	abstract fun getTitle(): Text
	open fun getPose(hat: HatData): HatPose = HatPose.SearchingHat

	open fun tick(world: ServerWorld, player: ServerPlayerEntity) {}
	open fun onLeftClick(world: ServerWorld, player: ServerPlayerEntity) {}
	open fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) {}
	open fun onLeftClickReleased(world: ServerWorld, player: ServerPlayerEntity) {}
	open fun onRightClickReleased(world: ServerWorld, player: ServerPlayerEntity) {}
	open fun onLeftClickHold(world: ServerWorld, player: ServerPlayerEntity) {}
	open fun onRightClickHold(world: ServerWorld, player: ServerPlayerEntity) {}

	fun markDirty(new: Ability?) {
		this.mutated = true
		this.replacement = new
	}

	companion object {
		val CODEC: Codec<Ability> = HattenedAbilities.ABILITY_REGISTRY.getCodec().dispatch("type", Ability::type, AbilityType<*>::codec)
	}
}