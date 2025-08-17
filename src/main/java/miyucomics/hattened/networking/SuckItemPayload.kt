package miyucomics.hattened.networking

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain.id
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class SuckItemPayload(val item: Int, val collector: Int, val amount: Int) : CustomPayload {
	override fun getId() = ID

	companion object {
		val ID: CustomPayload.Id<SuckItemPayload> = CustomPayload.Id(id("hat_keybind"))
		val CODEC = PacketCodecs.registryCodec(RecordCodecBuilder.create { builder ->
			builder.group(
				Codec.INT.fieldOf("item").forGetter(SuckItemPayload::item),
				Codec.INT.fieldOf("collector").forGetter(SuckItemPayload::collector),
				Codec.INT.fieldOf("amount").forGetter(SuckItemPayload::amount)
			).apply(builder, ::SuckItemPayload)
		})
	}
}