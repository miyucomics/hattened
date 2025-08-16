package miyucomics.hattened.structure

import com.mojang.datafixers.Products
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.Ability
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity

data class HatData(val hasHat: Boolean = false, val abilities: List<Ability> = listOf(), val usingHat: Boolean = false, val leftClickHeld: Boolean = false, val rightClickHeld: Boolean = false) {
	val ability: Ability?
		get() = abilities.getOrNull(0)

	fun toItemStack() = ItemStack(HattenedMain.HAT_ITEM).apply { set(HattenedMain.ABILITIES_COMPONENT, abilities) }

	fun tick(player: ServerPlayerEntity) {
		HattenedHelper.setPose(player, HatPose.OnHead)
		val selectedAbility = this.ability
		if (this.usingHat && selectedAbility != null) {
			selectedAbility.tick(player.world, player)
			if (this.leftClickHeld)
				selectedAbility.onLeftClickHold(player.world, player)
			if (this.rightClickHeld)
				selectedAbility.onRightClickHold(player.world, player)
			HattenedHelper.setPose(player, selectedAbility.getPose(this))
		}
	}

	fun transition(player: ServerPlayerEntity, event: UserInput): HatData {
		return when (event) {
			UserInput.LeftAltPressed -> this.copy(usingHat = true, leftClickHeld = false, rightClickHeld = false)
			UserInput.LeftAltReleased -> this.copy(usingHat = false, leftClickHeld = false, rightClickHeld = false)
			UserInput.ScrollUp -> this.copy(abilities = this.abilities.rotateLeft(), leftClickHeld = false, rightClickHeld = false)
			UserInput.ScrollDown -> this.copy(abilities = this.abilities.rotateRight(), leftClickHeld = false, rightClickHeld = false)
			UserInput.LeftMousePressed -> {
				this.ability?.onLeftClick(player.world, player)
				this.copy(leftClickHeld = true)
			}
			UserInput.LeftMouseReleased -> {
				this.ability?.onLeftClickReleased(player.world, player)
				this.copy(leftClickHeld = false)
			}
			UserInput.RightMousePressed -> {
				this.ability?.onRightClick(player.world, player)
				this.copy(rightClickHeld = true)
			}
			UserInput.RightMouseReleased -> {
				this.ability?.onRightClickReleased(player.world, player)
				this.copy(rightClickHeld = false)
			}
		}
	}

	companion object {
		@JvmField
		var DEFAULT = HatData()

		private fun commonFields(builder: RecordCodecBuilder.Instance<HatData>): Products.P2<RecordCodecBuilder.Mu<HatData>, Boolean, List<Ability>> {
			return builder.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat),
				Ability.CODEC.listOf().fieldOf("abilities").forGetter(HatData::abilities)
			)
		}

		var CODEC: Codec<HatData> = RecordCodecBuilder.create { commonFields(it).apply(it, ::HatData) }
		var PACKET_CODEC: PacketCodec<ByteBuf, HatData> = PacketCodecs.codec(RecordCodecBuilder.create {
			commonFields(it)
				.and(Codec.BOOL.fieldOf("usingHat").forGetter(HatData::usingHat))
				.and(Codec.BOOL.fieldOf("leftClickHeld").forGetter(HatData::leftClickHeld))
				.and(Codec.BOOL.fieldOf("rightClickHeld").forGetter(HatData::rightClickHeld))
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