package miyucomics.hattened.misc

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs

data class HatData(val hasHat: Boolean, val hatPose: HatPose) {
	constructor(hasHat: Boolean, hatPose: Int) : this(hasHat, enumValues<HatPose>()[hatPose])

	companion object {
		@JvmField
		var DEFAULT = HatData(false, HatPose.ON_HEAD)
		var CODEC: Codec<HatData> = RecordCodecBuilder.create { it.group(Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat)).apply(it) { hasHat -> HatData(hasHat, HatPose.ON_HEAD) } }
		var PACKET_CODEC: PacketCodec<ByteBuf, HatData> = PacketCodecs.codec(RecordCodecBuilder.create {
			it.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat),
				Codec.INT.fieldOf("usingHat").forGetter { hat -> hat.hatPose.ordinal },
			).apply(it, ::HatData)
		})
	}
}