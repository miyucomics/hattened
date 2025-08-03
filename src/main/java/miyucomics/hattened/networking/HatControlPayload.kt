package miyucomics.hattened.networking

import com.mojang.serialization.Codec
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.misc.HatPose
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

data class HatControlPayload(val hatPose: HatPose) : CustomPayload {
	constructor(hatPose: Int) : this(enumValues<HatPose>()[hatPose])

	override fun getId() = ID
	companion object {
		val ID: CustomPayload.Id<HatControlPayload> = CustomPayload.Id(HattenedMain.id("hat_control"))
		val CODEC: PacketCodec<RegistryByteBuf, HatControlPayload> = PacketCodec.tuple(PacketCodecs.codec(Codec.INT), { it.hatPose.ordinal }, ::HatControlPayload)
	}
}