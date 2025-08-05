package miyucomics.hattened.attach

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.enums.HatPose
import miyucomics.hattened.enums.UserInput
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs

data class HatState(val hatPose: HatPose) {
	constructor(hatPose: Int) : this(enumValues<HatPose>()[hatPose])

	fun transition(event: UserInput): HatState {
		return HatState(when (hatPose) {
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
		})
	}

	companion object {
		@JvmField
		var DEFAULT = HatState(HatPose.OnHead)
		var PACKET_CODEC: PacketCodec<ByteBuf, HatState> = PacketCodecs.codec(RecordCodecBuilder.create { it.group(Codec.INT.fieldOf("hatPose").forGetter { hat -> hat.hatPose.ordinal },).apply(it, ::HatState) })
	}
}