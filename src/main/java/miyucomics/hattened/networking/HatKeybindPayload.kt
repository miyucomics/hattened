package miyucomics.hattened.networking

import miyucomics.hattened.HattenedMain
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

class HatKeybindPayload : CustomPayload {
	override fun getId() = ID
	companion object {
		val ID: CustomPayload.Id<HatKeybindPayload> = CustomPayload.Id(HattenedMain.id("hat_keybind"))
		val CODEC: PacketCodec<RegistryByteBuf, HatKeybindPayload> = PacketCodec.of({ _, _ -> }, { HatKeybindPayload() })
	}
}