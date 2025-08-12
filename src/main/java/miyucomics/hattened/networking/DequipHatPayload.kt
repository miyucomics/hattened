package miyucomics.hattened.networking

import miyucomics.hattened.HattenedMain
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

class DequipHatPayload : CustomPayload {
	override fun getId() = ID
	companion object {
		val ID: CustomPayload.Id<DequipHatPayload> = CustomPayload.Id(HattenedMain.id("dequip"))
		val CODEC: PacketCodec<RegistryByteBuf, DequipHatPayload> = PacketCodec.of({ _, _ -> }, { DequipHatPayload() })
	}
}