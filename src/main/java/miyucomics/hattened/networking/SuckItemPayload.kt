package miyucomics.hattened.networking

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain.id
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class SuckItemPayload(val item: Int, val collector: Int) : CustomPayload {
	override fun getId() = ID

	companion object {
		val ID: CustomPayload.Id<SuckItemPayload> = CustomPayload.Id(id("hat_keybind"))
		val CODEC: PacketCodec<RegistryByteBuf, SuckItemPayload> = PacketCodecs.registryCodec(RecordCodecBuilder.create { builder ->
			builder.group(
				Codec.INT.fieldOf("item").forGetter(SuckItemPayload::item),
				Codec.INT.fieldOf("collector").forGetter(SuckItemPayload::collector)
			).apply(builder, ::SuckItemPayload)
		})
	}
}