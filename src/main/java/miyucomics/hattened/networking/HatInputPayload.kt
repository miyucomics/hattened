package miyucomics.hattened.networking

import com.mojang.serialization.Codec
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.enums.UserInput
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

data class HatInputPayload(val input: UserInput) : CustomPayload {
	constructor(hatPose: Int) : this(enumValues<UserInput>()[hatPose])

	override fun getId() = ID
	companion object {
		val ID: CustomPayload.Id<HatInputPayload> = CustomPayload.Id(HattenedMain.id("hat_control"))
		val CODEC: PacketCodec<RegistryByteBuf, HatInputPayload> = PacketCodec.tuple(PacketCodecs.codec(Codec.INT), { it.input.ordinal }, ::HatInputPayload)
	}
}