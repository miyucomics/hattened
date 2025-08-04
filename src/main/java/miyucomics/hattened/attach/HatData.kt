package miyucomics.hattened.attach

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs

data class HatData(val hasHat: Boolean) {
	companion object {
		@JvmField
		var DEFAULT = HatData(false)
		var CODEC: Codec<HatData> = RecordCodecBuilder.create { it.group(Codec.BOOL.fieldOf("hasHat").forGetter(HatData::hasHat)).apply(it, ::HatData) }
		var PACKET_CODEC: PacketCodec<ByteBuf, HatData> = PacketCodecs.codec(CODEC)
	}
}