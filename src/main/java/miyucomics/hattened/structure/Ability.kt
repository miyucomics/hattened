package miyucomics.hattened.structure

import com.mojang.serialization.Codec
import miyucomics.hattened.inits.HattenedAbilities
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

interface Ability {
	val type: AbilityType<*>

	fun getTitle(): Text
	fun getPose(): HatPose
	fun onTick(world: ServerWorld, player: ServerPlayerEntity) {}
	fun onLeftClick(world: ServerWorld, player: ServerPlayerEntity) {}
	fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) {}
	fun onLeftClickReleased(world: ServerWorld, player: ServerPlayerEntity) {}
	fun onRightClickReleased(world: ServerWorld, player: ServerPlayerEntity) {}

	companion object {
		val CODEC: Codec<Ability> = HattenedAbilities.ABILITY_REGISTRY.getCodec().dispatch("type", Ability::type, AbilityType<*>::codec)
	}
}