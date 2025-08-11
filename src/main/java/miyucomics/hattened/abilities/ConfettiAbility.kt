package miyucomics.hattened.abilities

import miyucomics.hattened.networking.ConfettiPayload
import miyucomics.hattened.structure.BaseAbility
import net.fabricmc.fabric.api.networking.v1.PlayerLookup.world
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.GoatEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

object ConfettiAbility : BaseAbility("confetti") {
	override fun onRightClick(player: ServerPlayerEntity) {
		player.world.players.forEach { player ->
			player.world.sendToPlayerIfNearby(player, false, player.x, player.y, player.z, ServerPlayNetworking.createS2CPacket(ConfettiPayload(0L, player.eyePos, player.rotationVector)))
		}
	}
}