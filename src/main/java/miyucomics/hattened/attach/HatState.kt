package miyucomics.hattened.attach

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.structure.HatPose
import miyucomics.hattened.structure.UserInput
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity

data class HatState(val selectedAbilityIndex: Int) {
	fun transition(player: ServerPlayerEntity, event: UserInput): HatState {
		val newSelectedAbilityIndex = (selectedAbilityIndex + when (event) {
			UserInput.ScrollUp -> -1
			UserInput.ScrollDown -> 1
			else -> 0
		}).mod(HattenedAbilities.ABILITY_REGISTRY.size())

		val ability = getAbility()
		when (event) {
			UserInput.LeftMousePressed -> ability.onLeftClick(player)
			UserInput.RightMousePressed -> ability.onRightClick(player)
			else -> {}
		}

		return HatState(newSelectedAbilityIndex)
	}

	fun getAbility() = HattenedAbilities.ABILITY_REGISTRY.get(selectedAbilityIndex)!!

	companion object {
		@JvmField
		var DEFAULT = HatState(0)
		var PACKET_CODEC: PacketCodec<ByteBuf, HatState> = PacketCodecs.codec(RecordCodecBuilder.create { it.group(
			Codec.INT.fieldOf("selectedAbility").forGetter { hat -> hat.selectedAbilityIndex }
		).apply(it, ::HatState) })
	}
}