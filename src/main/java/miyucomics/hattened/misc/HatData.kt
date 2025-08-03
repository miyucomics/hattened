package miyucomics.hattened.misc

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs

data class HatData(val hasHat: Boolean, val usingHat: Boolean) {
	fun isUsingHat() = hasHat && usingHat

	companion object {
		@JvmField
		var DEFAULT = HatData(hasHat = false, usingHat = false)
		var CODEC: Codec<HatData> = RecordCodecBuilder.create { it.group(Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat)).apply(it) { hasHat -> HatData(hasHat, false) } }
		var PACKET_CODEC: PacketCodec<ByteBuf, HatData> = PacketCodecs.codec(RecordCodecBuilder.create {
			it.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat),
				Codec.BOOL.fieldOf("usingHat").forGetter(HatData::usingHat),
			).apply(it, ::HatData)
		})
	}
}