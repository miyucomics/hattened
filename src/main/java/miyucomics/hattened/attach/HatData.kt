package miyucomics.hattened.attach

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.structure.Ability
import miyucomics.hattened.structure.UserInput
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

data class HatData(val hasHat: Boolean, val index: Int, val abilities: List<Identifier>) {
	val ability: Ability
		get() = HattenedAbilities.ABILITY_REGISTRY.get(abilities[index])!!

	fun toItemStack() = ItemStack(HattenedMain.HAT_ITEM).apply { set(HattenedMain.ABILITY_COMPONENT, abilities) }

	fun transition(player: ServerPlayerEntity, event: UserInput): HatData {
		val newIndex = (index + when (event) {
			UserInput.ScrollUp -> -1
			UserInput.ScrollDown -> 1
			else -> 0
		}).mod(HattenedAbilities.ABILITY_REGISTRY.size())

		when (event) {
			UserInput.LeftMousePressed -> this.ability.onLeftClick(player)
			UserInput.RightMousePressed -> this.ability.onRightClick(player)
			else -> {}
		}

		return this.getWithIndex(newIndex)
	}

	fun getWithHat(new: Boolean) = HatData(new, this.index, this.abilities)
	fun getWithIndex(new: Int) = HatData(this.hasHat, new, this.abilities)
	fun getWithAbilities(new: List<Identifier>) = HatData(this.hasHat, this.index, new)

	companion object {
		@JvmField
		var DEFAULT = HatData(false, 0, listOf())
		var CODEC: Codec<HatData> = RecordCodecBuilder.create {
			it.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat),
				Codec.INT.fieldOf("index").forGetter(HatData::index),
				Identifier.CODEC.listOf().fieldOf("abilities").forGetter(HatData::abilities)
			).apply(it, ::HatData)
		}
		var PACKET_CODEC: PacketCodec<ByteBuf, HatData> = PacketCodecs.codec(CODEC)
	}
}