package miyucomics.hattened.abilities

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import java.util.*

class ItemStackAbility(uuid: UUID, val stack: ItemStack) : Ability(TYPE, uuid) {
	constructor(stack: ItemStack) : this(UUID.randomUUID(), stack)
	private var cooldown = 0

	override fun getText(): Text = Text.translatable("ability.hattened.item_stack", stack.name)

	override fun tick(world: ServerWorld, player: ServerPlayerEntity) {
		this.cooldown--
		super.tick(world, player)
	}

	override fun onRightClickHold(world: ServerWorld, player: ServerPlayerEntity) {
		if (this.cooldown > 0)
			return

		val pos = player.eyePos
		val vel = player.rotationVector
		world.spawnEntity(ItemEntity(world, pos.x, pos.y, pos.z, this.stack.split(1), vel.x, vel.y, vel.z).apply { setPickupDelay(10) })
		world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS)

		if (stack.isEmpty)
			this.markDirty(null)
		else
			this.markDirty(ItemStackAbility(this.uuid, this.stack.copyAndEmpty()).also { it.cooldown = 2 })
	}

	companion object {
		val TYPE: AbilityType<ItemStackAbility> = object : AbilityType<ItemStackAbility>() {
			override val id = HattenedMain.id("item_stack")
			override val codec: MapCodec<ItemStackAbility> = RecordCodecBuilder.mapCodec { builder -> commonCodec(builder).and(ItemStack.CODEC.fieldOf("stack").forGetter(ItemStackAbility::stack)).apply(builder, ::ItemStackAbility) }
		}
	}
}