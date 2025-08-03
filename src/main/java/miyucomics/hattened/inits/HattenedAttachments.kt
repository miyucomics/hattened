package miyucomics.hattened.inits

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.misc.HatData
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType

@Suppress("UnstableAPIUsage")
object HattenedAttachments {
	@JvmField
	val HAT_DATA: AttachmentType<HatData> = AttachmentRegistry.create(HattenedMain.id("hat")) {
		it
			.initializer { -> HatData.DEFAULT }
			.persistent(HatData.CODEC)
			.syncWith(HatData.PACKET_CODEC, AttachmentSyncPredicate.all())
	}

	fun init() {}
}