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

data class HatData(val hasHat: Boolean = false, val storage: List<ServerCard> = listOf(), val usingHat: Boolean = false, val leftClickHeld: Boolean = false, val rightClickHeld: Boolean = false, val cooldown: Int = 0) {
	fun toItemStack() = ItemStack(HattenedMain.HAT_ITEM).apply { set(HattenedMain.HAT_STORAGE_COMPONENT, storage) }

	fun tick(player: ServerPlayerEntity) {
		HattenedHelper.setPose(player, HatPose.OnHead)
		val world = player.world
		val selectedCard = storage.firstOrNull()
		if (this.usingHat && selectedCard != null && leftClickHeld && this.cooldown <= 0) {
			val pos = player.eyePos
			val vel = player.rotationVector
			world.spawnEntity(ItemEntity(world, pos.x, pos.y, pos.z, selectedCard.stack.split(1), vel.x, vel.y, vel.z).apply { setPickupDelay(10) })
			world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS)
			(player as ServerPlayerEntityMinterface).`hattened$setCooldown`(5)
			selectedCard.markDirty()
		}
	}

	fun transition(event: UserInput): HatData {
		return when (event) {
			UserInput.LeftAltPressed -> this.copy(usingHat = true, leftClickHeld = false, rightClickHeld = false)
			UserInput.LeftAltReleased -> this.copy(usingHat = false, leftClickHeld = false, rightClickHeld = false)
			UserInput.ScrollUp -> this.copy(storage = this.storage.rotateLeft(), leftClickHeld = false, rightClickHeld = false)
			UserInput.ScrollDown -> this.copy(storage = this.storage.rotateRight(), leftClickHeld = false, rightClickHeld = false)
			UserInput.LeftMousePressed -> this.copy(leftClickHeld = true)
			UserInput.LeftMouseReleased -> this.copy(leftClickHeld = false)
			UserInput.RightMousePressed -> this.copy(rightClickHeld = true)
			UserInput.RightMouseReleased -> this.copy(rightClickHeld = false)
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
				.and(Codec.BOOL.fieldOf("leftClickHeld").forGetter(HatData::leftClickHeld))
				.and(Codec.BOOL.fieldOf("rightClickHeld").forGetter(HatData::rightClickHeld))
				.and(Codec.INT.fieldOf("cooldown").forGetter(HatData::cooldown))
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