package miyucomics.hattened.inits

import miyucomics.hattened.HattenedHelper
import miyucomics.hattened.attach.HatData
import miyucomics.hattened.networking.ConfettiPayload
import miyucomics.hattened.networking.DequipHatPayload
import miyucomics.hattened.networking.HatInputPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Hand

@Suppress("UnstableAPIUsage")
object HattenedNetworking {
	fun init() {
		PayloadTypeRegistry.playS2C().register(ConfettiPayload.ID, ConfettiPayload.CODEC)
		PayloadTypeRegistry.playC2S().register(HatInputPayload.ID, HatInputPayload.CODEC)
		PayloadTypeRegistry.playC2S().register(DequipHatPayload.ID, DequipHatPayload.CODEC)

		ServerPlayNetworking.registerGlobalReceiver(HatInputPayload.ID) { payload, context ->
			val player = context.player()
			val before = HattenedHelper.getHatData(player)
			HattenedHelper.setHatData(player, before.transition(player, payload.input))
		}

		ServerPlayNetworking.registerGlobalReceiver(DequipHatPayload.ID) { _, context ->
			val player = context.player()
			if (player.getStackInHand(Hand.MAIN_HAND).isEmpty) {
				player.swingHand(Hand.MAIN_HAND, true)
				player.setStackInHand(Hand.MAIN_HAND, HattenedHelper.getHatData(player).toItemStack())
				HattenedHelper.setHatData(player, HatData(false, 0, listOf()))
			}
		}
	}
}