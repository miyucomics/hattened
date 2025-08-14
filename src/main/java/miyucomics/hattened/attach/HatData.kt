package miyucomics.hattened.attach

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.Ability
import miyucomics.hattened.structure.HatPose
import miyucomics.hattened.structure.UserInput
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity

data class HatData(val hasHat: Boolean, val usingHat: Boolean, val index: Int, val abilities: List<Ability>) {
	constructor(hasHat: Boolean, index: Int, abilities: List<Ability>) : this(hasHat = hasHat, usingHat = false, index, abilities)

	val ability: Ability?
		get() = abilities.getOrNull(index)

	fun toItemStack() = ItemStack(HattenedMain.HAT_ITEM).apply { set(HattenedMain.ABILITY_COMPONENT, abilities) }

	fun tick(player: ServerPlayerEntity) {
		if (usingHat)
			this.ability?.tick(player.world, player)
	}

	fun transition(player: ServerPlayerEntity, event: UserInput): HatData {
		var newHat = this

		when (event) {
			UserInput.LeftAltPressed -> {
				this.ability?.switchOff(player.world, player)
				newHat = newHat.copy(usingHat = true)
			}
			UserInput.LeftAltReleased -> {
				this.ability?.switchOff(player.world, player)
				newHat = newHat.copy(usingHat = false)
			}
			UserInput.LeftMousePressed -> this.ability?.onLeftClick(player.world, player)
			UserInput.LeftMouseReleased -> this.ability?.onLeftClickReleased(player.world, player)
			UserInput.RightMousePressed -> this.ability?.onRightClick(player.world, player)
			UserInput.RightMouseReleased -> this.ability?.onRightClickReleased(player.world, player)
			UserInput.ScrollUp -> {
				this.ability?.switchOff(player.world, player)
				newHat = newHat.copy(index = (newHat.index - 1).mod(abilities.size))
			}
			UserInput.ScrollDown ->  {
				this.ability?.switchOff(player.world, player)
				newHat = newHat.copy(index = (newHat.index + 1).mod(abilities.size))
			}
		}

		return newHat
	}

	fun getPose(): HatPose {
		if (!this.usingHat)
			return HatPose.OnHead
		return this.ability?.getPose() ?: HatPose.SearchingHat
	}

	companion object {
		@JvmField
		var DEFAULT = HatData(hasHat = false, usingHat = false, index = 0, abilities = listOf())
		var CODEC: Codec<HatData> = RecordCodecBuilder.create {
			it.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat),
				Codec.INT.fieldOf("index").forGetter(HatData::index),
				Ability.CODEC.listOf().fieldOf("abilities").forGetter(HatData::abilities)
			).apply(it, ::HatData)
		}
		var PACKET_CODEC: PacketCodec<ByteBuf, HatData> = PacketCodecs.codec(RecordCodecBuilder.create {
			it.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat),
				Codec.BOOL.fieldOf("usingHat").forGetter(HatData::usingHat),
				Codec.INT.fieldOf("index").forGetter(HatData::index),
				Ability.CODEC.listOf().fieldOf("abilities").forGetter(HatData::abilities)
			).apply(it, ::HatData)
		})
	}
}