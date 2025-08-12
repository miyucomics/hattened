package miyucomics.hattened.inits

import miyucomics.hattened.HattenedHelper
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
			val before = HattenedHelper.getHatData(context.player())
			context.player().setAttached(HattenedAttachments.HAT_DATA, before.transition(context.player(), payload.input))
		}
	}
}