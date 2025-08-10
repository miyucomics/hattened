package miyucomics.hattened.inits

import miyucomics.hattened.attach.HatState
import miyucomics.hattened.networking.HatInputPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Suppress("UnstableAPIUsage")
object HattenedNetworking {
	fun init() {
		PayloadTypeRegistry.playC2S().register(HatInputPayload.ID, HatInputPayload.CODEC)
		ServerPlayNetworking.registerGlobalReceiver(HatInputPayload.ID) { payload, context ->
			val before = context.player().getAttachedOrSet(HattenedAttachments.HAT_STATE_DATA, HatState.DEFAULT)
			context.player().setAttached(HattenedAttachments.HAT_STATE_DATA, before.transition(context.player(), payload.input))
		}
	}
}