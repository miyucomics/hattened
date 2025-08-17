package miyucomics.hattened.structure

import com.mojang.datafixers.Products
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.HattenedMain
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents

data class HatData(val hasHat: Boolean = false, val storage: List<ServerCard> = listOf(), val usingHat: Boolean = false, val isThrowingItems: Boolean = false, val isVacuuming: Boolean = false) {
	fun toItemStack() = ItemStack(HattenedMain.HAT_ITEM).apply { set(HattenedMain.HAT_STORAGE_COMPONENT, storage) }

	fun tick(player: ServerPlayerEntity) {
		HattenedHelper.setPose(player, HatPose.OnHead)
		val world = player.world
		val selectedCard = storage.firstOrNull()
		if (this.usingHat)
			HattenedHelper.setPose(player, HatPose.SearchingHat)

		if (this.usingHat && this.isThrowingItems && selectedCard != null) {
			val pos = player.pos.add(0.0, 0.5, 0.0)
			val vel = player.rotationVector
			world.spawnEntity(ItemEntity(world, pos.x, pos.y, pos.z, selectedCard.stack.split(1), vel.x, vel.y, vel.z).apply { setPickupDelay(10) })
			world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS)
			selectedCard.markDirty()
		}
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
		}
	}

	companion object {
		@JvmField
		var DEFAULT = HatData()

		private fun commonFields(builder: RecordCodecBuilder.Instance<HatData>): Products.P2<RecordCodecBuilder.Mu<HatData>, Boolean, List<ServerCard>> {
			return builder.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat),
				ServerCard.CODEC.listOf().fieldOf("storage").forGetter(HatData::storage)
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
	}
}

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