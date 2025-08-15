package miyucomics.hattened.structure

import com.mojang.datafixers.Products
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.Ability
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.server.network.ServerPlayerEntity

data class HatDataAttachment(val hasHat: Boolean = false, val index: Int = 0, val abilities: List<Ability> = listOf(), val usingHat: Boolean = false) {
	constructor(hasHat: Boolean, index: Int, abilities: List<Ability>) : this(hasHat = hasHat, index, abilities, usingHat = false)

	val ability: Ability?
		get() = abilities.getOrNull(index)

	fun toItemStack() = ItemStack(HattenedMain.HAT_ITEM).apply { set(HattenedMain.ABILITY_COMPONENT, abilities) }

	fun tick(player: ServerPlayerEntity): HatDataAttachment {
		HattenedHelper.setPose(player, HatPose.OnHead)
		val selectedAbility = this.ability
		if (this.usingHat && selectedAbility != null) {
			selectedAbility.tick(player.world, player)
			HattenedHelper.setPose(player, selectedAbility.getPose())
		}
		val remainingAbilities = abilities.filterNot { it.removable }
		val newIndex = if (remainingAbilities.isEmpty()) 0 else index % remainingAbilities.size
		return this.copy(abilities = remainingAbilities, index = newIndex)
	}

	fun transition(player: ServerPlayerEntity, event: UserInput): HatDataAttachment? {
		when (event) {
			UserInput.LeftAltPressed -> {
				this.ability?.switchOff(player.world, player)
				return this.copy(usingHat = true)
			}
			UserInput.LeftAltReleased -> {
				this.ability?.switchOff(player.world, player)
				return this.copy(usingHat = false)
			}
			UserInput.ScrollUp -> {
				this.ability?.switchOff(player.world, player)
				return this.copy(index = (this.index - 1).mod(abilities.size))
			}
			UserInput.ScrollDown ->  {
				this.ability?.switchOff(player.world, player)
				return this.copy(index = (this.index + 1).mod(abilities.size))
			}
			UserInput.LeftMousePressed -> {
				this.ability?.onLeftClick(player.world, player)
				return null
			}
			UserInput.LeftMouseReleased -> {
				this.ability?.onLeftClickReleased(player.world, player)
				return null
			}
			UserInput.RightMousePressed -> {
				this.ability?.onRightClick(player.world, player)
				return null
			}
			UserInput.RightMouseReleased -> {
				this.ability?.onRightClickReleased(player.world, player)
				return null
			}
		}
	}

	companion object {
		@JvmField
		var DEFAULT = HatDataAttachment()

		private fun commonFields(builder: RecordCodecBuilder.Instance<HatDataAttachment>): Products.P3<RecordCodecBuilder.Mu<HatDataAttachment>, Boolean, Int, List<Ability>> {
			return builder.group(
				Codec.BOOL.fieldOf("hasHat").forGetter(HatDataAttachment::hasHat),
				Codec.INT.fieldOf("index").forGetter(HatDataAttachment::index),
				Ability.Companion.CODEC.listOf().fieldOf("abilities").forGetter(HatDataAttachment::abilities)
			)
		}

		var CODEC: Codec<HatDataAttachment> = RecordCodecBuilder.create { commonFields(it).apply(it, ::HatDataAttachment) }
		var PACKET_CODEC: PacketCodec<ByteBuf, HatDataAttachment> = PacketCodecs.codec(RecordCodecBuilder.create { commonFields(it).and(
			Codec.BOOL.fieldOf("usingHat").forGetter(HatDataAttachment::usingHat)).apply(it, ::HatDataAttachment) })
	}
}