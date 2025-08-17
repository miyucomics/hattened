package miyucomics.hattened.inits

import miyucomics.hattened.networking.ConfettiPayload
import miyucomics.hattened.networking.HatInputPayload
import miyucomics.hattened.networking.HatKeybindPayload
import miyucomics.hattened.structure.HatData
import miyucomics.hattened.structure.HattenedHelper
import miyucomics.hattened.structure.ServerPlayerEntityMinterface
import miyucomics.hattened.structure.UserInput
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand

object HattenedNetworking {
	fun init() {
		PayloadTypeRegistry.playS2C().register(ConfettiPayload.ID, ConfettiPayload.CODEC)
		PayloadTypeRegistry.playC2S().register(HatInputPayload.ID, HatInputPayload.CODEC)
		PayloadTypeRegistry.playC2S().register(HatKeybindPayload.ID, HatKeybindPayload.CODEC)

		ServerPlayNetworking.registerGlobalReceiver(HatInputPayload.ID) { payload, context ->
			val player = context.player()
			val world = player.world
			(context.player() as ServerPlayerEntityMinterface).queueUserInput(payload.input)
			if (payload.input == UserInput.MiddleMousePressed)
				world.players.forEach { world.sendToPlayerIfNearby(it, false, player.x, player.y, player.z, ServerPlayNetworking.createS2CPacket(ConfettiPayload(0L, player.pos.add(0.0, 0.75, 0.0), player.rotationVector))) }
		}

		ServerPlayNetworking.registerGlobalReceiver(HatKeybindPayload.ID) { _, context ->
			val player = context.player()
			val hat = HattenedHelper.getHatData(player)
			if (player.getStackInHand(Hand.MAIN_HAND).isEmpty && hat.hasHat) {
				player.swingHand(Hand.MAIN_HAND, true)
				player.setStackInHand(Hand.MAIN_HAND, hat.toItemStack())
				HattenedHelper.setHatData(player, HatData(false, listOf()))
				player.world.playSound(null, player.x, player.y, player.z, HattenedSounds.HAT_EQUIP, SoundCategory.PLAYERS, 1f, 1f)
			}
		}
	}
}