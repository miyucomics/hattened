package miyucomics.hattened.attach

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.structure.HatPose
import miyucomics.hattened.structure.UserInput
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs

data class HatState(val hatPose: HatPose, val selectedAbilityIndex: Int) {
	constructor(hatPose: Int, selectedAbilityIndex: Int) : this(enumValues<HatPose>()[hatPose], selectedAbilityIndex)

	fun transition(event: UserInput): HatState {
		val newHatPose = when (hatPose) {
			HatPose.OnHead -> when (event) {
				UserInput.LeftAltPressed -> HatPose.SearchingHat
				else -> hatPose
			}
			HatPose.SearchingHat -> when (event) {
				UserInput.LeftAltReleased -> HatPose.OnHead
				UserInput.RightMousePressed -> HatPose.Vacuuming
				UserInput.LeftMousePressed -> HatPose.Bowing
				else -> hatPose
			}
			HatPose.Vacuuming -> when (event) {
				UserInput.RightMouseReleased -> HatPose.SearchingHat
				UserInput.LeftAltReleased -> HatPose.OnHead
				else -> hatPose
			}
			HatPose.Bowing -> when (event) {
				UserInput.LeftMouseReleased -> HatPose.SearchingHat
				UserInput.LeftAltReleased -> HatPose.OnHead
				else -> hatPose
			}
		}

		val newSelectedAbilityIndex = selectedAbilityIndex + when (event) {
			UserInput.ScrollUp -> -1
			UserInput.ScrollDown -> 1
			else -> 0
		}

		return HatState(newHatPose, newSelectedAbilityIndex.mod(HattenedAbilities.ABILITY_REGISTRY.size()))
	}

	companion object {
		@JvmField
		var DEFAULT = HatState(HatPose.OnHead, 0)
		var PACKET_CODEC: PacketCodec<ByteBuf, HatState> = PacketCodecs.codec(RecordCodecBuilder.create { it.group(
			Codec.INT.fieldOf("hatPose").forGetter { hat -> hat.hatPose.ordinal },
			Codec.INT.fieldOf("selectedAbility").forGetter { hat -> hat.selectedAbilityIndex }
		).apply(it, ::HatState) })
	}
}