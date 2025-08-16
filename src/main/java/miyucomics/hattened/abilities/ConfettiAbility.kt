package miyucomics.hattened.abilities

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.networking.ConfettiPayload
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.*

class ConfettiAbility(uuid: UUID) : Ability(TYPE, uuid) {
	constructor() : this(UUID.randomUUID())
	override fun getText(): Text = Text.translatable("ability.hattened.confetti")

	override fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) {
		world.players.forEach { player ->
			world.sendToPlayerIfNearby(player, false, player.x, player.y, player.z, ServerPlayNetworking.createS2CPacket(ConfettiPayload(0L, player.eyePos, player.rotationVector)))
		}
	}

	companion object {
		val TYPE: AbilityType<ConfettiAbility> = object : AbilityType<ConfettiAbility>() {
			override val id = HattenedMain.id("confetti")
			override val codec: MapCodec<ConfettiAbility> = RecordCodecBuilder.mapCodec { builder -> commonCodec(builder).apply(builder, ::ConfettiAbility) }
		}
	}
}