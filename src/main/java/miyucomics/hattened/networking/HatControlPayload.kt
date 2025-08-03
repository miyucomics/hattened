package miyucomics.hattened.networking

import com.mojang.serialization.Codec
import miyucomics.hattened.HattenedMain
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

data class HatControlPayload(val isUsingHat: Boolean) : CustomPayload {
	override fun getId() = ID
	companion object {
		val ID: CustomPayload.Id<HatControlPayload> = CustomPayload.Id(HattenedMain.id("hat_control"))
		val CODEC: PacketCodec<RegistryByteBuf, HatControlPayload> = PacketCodec.tuple(PacketCodecs.codec(Codec.BOOL), HatControlPayload::isUsingHat, ::HatControlPayload)
	}
}