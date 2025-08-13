package miyucomics.hattened.abilities

import com.mojang.serialization.Decoder
import com.mojang.serialization.Encoder
import com.mojang.serialization.MapCodec
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.networking.ConfettiPayload
import miyucomics.hattened.structure.Ability
import miyucomics.hattened.structure.AbilityType
import miyucomics.hattened.structure.HatPose
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

class ConfettiAbility : Ability {
	override val type = TYPE

	override fun getPose() = HatPose.SearchingHat
	override fun getTitle(): Text = Text.translatable("ability.hattened.confetti")

	override fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) {
		world.players.forEach { player ->
			world.sendToPlayerIfNearby(player, false, player.x, player.y, player.z, ServerPlayNetworking.createS2CPacket(ConfettiPayload(0L, player.eyePos, player.rotationVector)))
		}
	}

	companion object {
		var TYPE: AbilityType<ConfettiAbility> = object : AbilityType<ConfettiAbility>() {
			override val argc: Int = 0
			override val id = HattenedMain.id("confetti")
			override val codec: MapCodec<ConfettiAbility> = MapCodec.of(Encoder.empty(), Decoder.unit { -> ConfettiAbility() })
		}
	}
}