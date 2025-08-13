package miyucomics.hattened.inits

import miyucomics.hattened.HattenedHelper
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.attach.HatData
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity

@Suppress("UnstableAPIUsage")
object HattenedAttachments {
	@JvmField val HAT_DATA: AttachmentType<HatData> = AttachmentRegistry.create(HattenedMain.id("hat")) { it.initializer { -> HatData.DEFAULT }.persistent(HatData.CODEC).syncWith(HatData.PACKET_CODEC, AttachmentSyncPredicate.all()) }

	fun init() {
		ServerLivingEntityEvents.AFTER_DEATH.register { entity, _ ->
			if (entity is PlayerEntity) {
				val hat = HattenedHelper.getHatData(entity)
				if (hat.hasHat)
					entity.world.spawnEntity(ItemEntity(entity.world, entity.x, entity.y, entity.z, hat.toItemStack()))
			}
		}
	}
}