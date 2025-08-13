package miyucomics.hattened.abilities

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.structure.Ability
import miyucomics.hattened.structure.AbilityType
import miyucomics.hattened.structure.HatPose
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

class ItemStackAbility(val stack: ItemStack) : Ability {
	override val type = TYPE

	override fun getPose() = HatPose.SearchingHat
	override fun getTitle(): Text = Text.translatable("ability.hattened.item_stack", stack.name)

	override fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) {
		val position = player.eyePos
		val velocity = player.rotationVector.multiply(1.5)
		world.spawnEntity(ItemEntity(world, position.x, position.y, position.z, stack, velocity.x, velocity.y, velocity.z).apply {
			setPickupDelay(10)
		})
	}

	companion object {
		var TYPE: AbilityType<ItemStackAbility> = object : AbilityType<ItemStackAbility>() {
			override val argc: Int = 0
			override val id = HattenedMain.id("item_stack")
			override val codec: MapCodec<ItemStackAbility> = RecordCodecBuilder.mapCodec { it.group(ItemStack.CODEC.fieldOf("stack").forGetter(ItemStackAbility::stack)).apply(it, ::ItemStackAbility) }
		}
	}
}