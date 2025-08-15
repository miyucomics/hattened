package miyucomics.hattened.inits

import miyucomics.hattened.HattenedHelper
import miyucomics.hattened.attach.HatDataAttachment
import miyucomics.hattened.networking.ConfettiPayload
import miyucomics.hattened.networking.DequipHatPayload
import miyucomics.hattened.networking.HatInputPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Hand

object HattenedNetworking {
	fun init() {
		PayloadTypeRegistry.playS2C().register(ConfettiPayload.ID, ConfettiPayload.CODEC)
		PayloadTypeRegistry.playC2S().register(HatInputPayload.ID, HatInputPayload.CODEC)
		PayloadTypeRegistry.playC2S().register(DequipHatPayload.ID, DequipHatPayload.CODEC)

		ServerPlayNetworking.registerGlobalReceiver(HatInputPayload.ID) { payload, context ->
			val player = context.player()
			val before = HattenedHelper.getHatData(player)
			val newHat = before.transition(player, payload.input)
			if (newHat != null)
				HattenedHelper.setHatData(player, newHat)
		}

		ServerPlayNetworking.registerGlobalReceiver(DequipHatPayload.ID) { _, context ->
			val player = context.player()
			val hat = HattenedHelper.getHatData(player)
			if (player.getStackInHand(Hand.MAIN_HAND).isEmpty && hat.hasHat) {
				player.swingHand(Hand.MAIN_HAND, true)
				player.setStackInHand(Hand.MAIN_HAND, hat.toItemStack())
				HattenedHelper.setHatData(player, HatDataAttachment(false, 0, listOf()))
			}
		}
	}
}