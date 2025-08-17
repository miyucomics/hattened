package miyucomics.hattened.networking

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.Vec3d

data class ConfettiPayload(val seed: Long, val position: Vec3d, val direction: Vec3d) : CustomPayload {
	override fun getId() = ID
	companion object {
		val ID: CustomPayload.Id<ConfettiPayload> = CustomPayload.Id(HattenedMain.id("confetti"))
		val CODEC: PacketCodec<RegistryByteBuf, ConfettiPayload> = PacketCodecs.registryCodec(RecordCodecBuilder.create { builder ->
			builder.group(
				Codec.LONG.fieldOf("seed").forGetter(ConfettiPayload::seed),
				Vec3d.CODEC.fieldOf("position").forGetter(ConfettiPayload::position),
				Vec3d.CODEC.fieldOf("direction").forGetter(ConfettiPayload::direction)
			).apply(builder, ::ConfettiPayload)
		})
	}
}