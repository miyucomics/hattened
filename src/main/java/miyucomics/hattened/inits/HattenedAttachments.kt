package miyucomics.hattened.inits

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.attach.HatData
import miyucomics.hattened.attach.HatState
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType

@Suppress("UnstableAPIUsage")
object HattenedAttachments {
	@JvmField val HAT_DATA: AttachmentType<HatData> = AttachmentRegistry.create(HattenedMain.id("hat")) { it.initializer { -> HatData.DEFAULT }.persistent(HatData.CODEC).syncWith(HatData.PACKET_CODEC, AttachmentSyncPredicate.all()) }
	@JvmField val HAT_STATE_DATA: AttachmentType<HatState> = AttachmentRegistry.create(HattenedMain.id("hat_state")) { it.initializer { -> HatState.DEFAULT }.syncWith(HatState.PACKET_CODEC, AttachmentSyncPredicate.all()) }

	fun init() {}
}