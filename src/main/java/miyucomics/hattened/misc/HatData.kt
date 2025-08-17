package miyucomics.hattened.misc

import com.mojang.datafixers.Products
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.inits.HattenedSounds
import miyucomics.hattened.networking.SuckItemPayload
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand

data class HatData(val hasHat: Boolean = false, val storage: List<Card> = listOf(), val usingHat: Boolean = false, val isThrowingItems: Boolean = false, val isVacuuming: Boolean = false) {
	fun toItemStack() = ItemStack(HattenedMain.HAT_ITEM).apply { set(HattenedMain.HAT_STORAGE_COMPONENT, storage) }

	fun tick(player: ServerPlayerEntity) {
		if (!this.usingHat) {
			HattenedHelper.setPose(player, HatPose.OnHead)
			return
		}

		val world = player.world
		val selectedCard = storage.firstOrNull()
		var pose = HatPose.SearchingHat

		if (!this.isVacuuming && this.isThrowingItems && selectedCard != null) {
			val pos = player.pos.add(0.0, 0.75, 0.0)
			val vel = player.rotationVector
			world.spawnEntity(ItemEntity(world, pos.x, pos.y, pos.z, selectedCard.stack.split(1), vel.x, vel.y, vel.z).apply { setPickupDelay(10) })
			world.playSound(null, pos.x, pos.y, pos.z, HattenedSounds.THROW_ITEM, SoundCategory.PLAYERS, 0.5f, 1f)
			player.swingHand(Hand.MAIN_HAND, true)
			selectedCard.markDirty()
		}

		if (this.isVacuuming) {
			pose = HatPose.Vacuuming
			world.getEntitiesByClass(ItemEntity::class.java, player.boundingBox.expand(24.0)) {
				val toItem = it.pos.subtract(player.pos).normalize()
				player.canSee(it) && !it.cannotPickup() && toItem.dotProduct(player.rotationVector) > 0.9f
			}.firstOrNull()?.let {
				(player as ServerPlayerEntityMinterface).proposeItemStack(it.stack.split(1))
				world.players.forEach { other -> world.sendToPlayerIfNearby(other, true, player.x, player.y, player.z, ServerPlayNetworking.createS2CPacket(SuckItemPayload(it.id, player.id, 1))) }
			}
		}

		HattenedHelper.setPose(player, pose)
	}

	fun updateInternalState(event: UserInput): HatData {
		return when (event) {
			UserInput.LeftAltPressed -> this.copy(usingHat = true, isThrowingItems = false, isVacuuming = false)
			UserInput.LeftAltReleased -> this.copy(usingHat = false, isThrowingItems = false, isVacuuming = false)
			UserInput.ScrollUp -> this.copy(storage = this.storage.rotateLeft(), isThrowingItems = false, isVacuuming = false)
			UserInput.ScrollDown -> this.copy(storage = this.storage.rotateRight(), isThrowingItems = false, isVacuuming = false)
			UserInput.LeftMousePressed -> this.copy(isThrowingItems = true)
			UserInput.LeftMouseReleased -> this.copy(isThrowingItems = false)
			UserInput.RightMousePressed -> this.copy(isVacuuming = true)
			UserInput.RightMouseReleased -> this.copy(isVacuuming = false)
			else -> this
		}
	}

	companion object {
		@JvmField
		var DEFAULT = HatData()

		private fun commonFields(builder: RecordCodecBuilder.Instance<HatData>): Products.P2<RecordCodecBuilder.Mu<HatData>, Boolean, List<Card>> {
			return builder.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat),
				Card.CODEC.listOf().fieldOf("storage").forGetter(HatData::storage)
			)
		}

		var CODEC: Codec<HatData> = RecordCodecBuilder.create { commonFields(it).apply(it, ::HatData) }
		var PACKET_CODEC: PacketCodec<ByteBuf, HatData> = PacketCodecs.codec(RecordCodecBuilder.create {
			commonFields(it)
				.and(Codec.BOOL.fieldOf("usingHat").forGetter(HatData::usingHat))
				.and(Codec.BOOL.fieldOf("throwing").forGetter(HatData::isThrowingItems))
				.and(Codec.BOOL.fieldOf("vacuuming").forGetter(HatData::isVacuuming))
				.apply(it, ::HatData)
		})

		private fun <E> List<E>.rotateLeft(): List<E> {
			if (this.isEmpty())
				return this
			return drop(1) + first()
		}

		private fun <E> List<E>.rotateRight(): List<E> {
			if (this.isEmpty())
				return this
			return takeLast(1) + dropLast(1)
		}
	}
}