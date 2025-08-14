package miyucomics.hattened.inits

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedHelper
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.attach.HatDataAttachment
import miyucomics.hattened.structure.HatPose
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.codec.PacketCodecs

@Suppress("UnstableAPIUsage")
object HattenedAttachments {
	val HAT_DATA: AttachmentType<HatDataAttachment> = AttachmentRegistry.create(HattenedMain.id("hat")) { builder ->
		builder
			.initializer { -> HatDataAttachment.DEFAULT }
			.persistent(HatDataAttachment.CODEC)
			.syncWith(HatDataAttachment.PACKET_CODEC, AttachmentSyncPredicate.all())
	}

	val HAT_POSE: AttachmentType<HatPose> = AttachmentRegistry.create(HattenedMain.id("hat_pose")) { builder ->
		builder
			.initializer { -> HatPose.OnHead }
			.syncWith(PacketCodecs.codec(RecordCodecBuilder.create { builder ->
				builder.group(Codec.INT.fieldOf("pose").forGetter { it.ordinal }).apply(builder) { enumValues<HatPose>()[it] }
			}), AttachmentSyncPredicate.all())
	}

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