package miyucomics.hattened.inits

import miyucomics.hattened.misc.HatData
import miyucomics.hattened.networking.HatControlPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object HattenedNetworking {
	fun init() {
		PayloadTypeRegistry.playC2S().register(HatControlPayload.ID, HatControlPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(HatControlPayload.ID) { payload, context ->
			context.player().setAttached(HattenedAttachments.HAT_DATA, HatData(true, payload.isUsingHat))
		}
	}
}