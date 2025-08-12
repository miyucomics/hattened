package miyucomics.hattened.inits

import miyucomics.hattened.attach.HatData
import miyucomics.hattened.networking.ConfettiPayload
import miyucomics.hattened.networking.HatInputPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Suppress("UnstableAPIUsage")
object HattenedNetworking {
	fun init() {
		PayloadTypeRegistry.playS2C().register(ConfettiPayload.ID, ConfettiPayload.CODEC)
		PayloadTypeRegistry.playC2S().register(HatInputPayload.ID, HatInputPayload.CODEC)
		ServerPlayNetworking.registerGlobalReceiver(HatInputPayload.ID) { payload, context ->
			val before = context.player().getAttachedOrSet(HattenedAttachments.HAT_STATE, HatData.DEFAULT)
			context.player().setAttached(HattenedAttachments.HAT_STATE, before.transition(context.player(), payload.input))
		}
	}
}