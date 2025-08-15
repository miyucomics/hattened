package miyucomics.hattened.attach

import com.mojang.datafixers.Products
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.Ability
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.structure.HatPose
import miyucomics.hattened.structure.UserInput
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity

data class HatDataAttachment(val hasHat: Boolean = false, val index: Int = 0, val abilities: List<Ability> = listOf(), val usingHat: Boolean = false) {
	constructor(hasHat: Boolean, index: Int, abilities: List<Ability>) : this(hasHat = hasHat, index, abilities, usingHat = false)

	val ability: Ability?
		get() = abilities.getOrNull(index)

	fun toItemStack() = ItemStack(HattenedMain.HAT_ITEM).apply { set(HattenedMain.ABILITY_COMPONENT, abilities) }

	fun tick(player: ServerPlayerEntity) {
		player.setAttached(HattenedAttachments.HAT_POSE, HatPose.OnHead)
		if (this.usingHat) {
			this.ability?.tick(player.world, player)
			player.setAttached(HattenedAttachments.HAT_POSE, this.ability?.getPose())
		}
	}

	fun transition(player: ServerPlayerEntity, event: UserInput): HatDataAttachment {
		var newHat = this

		when (event) {
			UserInput.LeftAltPressed -> {
				this.ability?.switchOff(player.world, player)
				newHat = newHat.copy(usingHat = true)
			}
			UserInput.LeftAltReleased -> {
				this.ability?.switchOff(player.world, player)
				newHat = newHat.copy(usingHat = false)
			}
			UserInput.LeftMousePressed -> this.ability?.onLeftClick(player.world, player)
			UserInput.LeftMouseReleased -> this.ability?.onLeftClickReleased(player.world, player)
			UserInput.RightMousePressed -> this.ability?.onRightClick(player.world, player)
			UserInput.RightMouseReleased -> this.ability?.onRightClickReleased(player.world, player)
			UserInput.ScrollUp -> {
				this.ability?.switchOff(player.world, player)
				newHat = newHat.copy(index = (newHat.index - 1).mod(abilities.size))
			}
			UserInput.ScrollDown ->  {
				this.ability?.switchOff(player.world, player)
				newHat = newHat.copy(index = (newHat.index + 1).mod(abilities.size))
			}
		}

		return newHat
	}

	companion object {
		@JvmField
		var DEFAULT = HatDataAttachment()

		private fun commonFields(builder: RecordCodecBuilder.Instance<HatDataAttachment>): Products.P3<RecordCodecBuilder.Mu<HatDataAttachment>, Boolean, Int, List<Ability>> {
			return builder.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatDataAttachment::hasHat),
				Codec.INT.fieldOf("index").forGetter(HatDataAttachment::index),
				Ability.CODEC.listOf().fieldOf("abilities").forGetter(HatDataAttachment::abilities)
			)
		}

		var CODEC: Codec<HatDataAttachment> = RecordCodecBuilder.create { commonFields(it).apply(it, ::HatDataAttachment) }
		var PACKET_CODEC: PacketCodec<ByteBuf, HatDataAttachment> = PacketCodecs.codec(RecordCodecBuilder.create { commonFields(it).and(Codec.BOOL.fieldOf("usingHat").forGetter(HatDataAttachment::usingHat)).apply(it, ::HatDataAttachment) })
	}
}