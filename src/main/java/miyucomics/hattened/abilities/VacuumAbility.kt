package miyucomics.hattened.abilities

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.structure.HatData
import miyucomics.hattened.structure.HatPose
import miyucomics.hattened.structure.HattenedHelper
import miyucomics.hattened.structure.ServerPlayerEntityMinterface
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.*
import kotlin.math.min

class VacuumAbility(uuid: UUID) : Ability(TYPE, uuid) {
	override fun getPose(hat: HatData): HatPose = if (hat.rightClickHeld) HatPose.Vacuuming else HatPose.SearchingHat
	override fun getTitle(): Text = Text.translatable("ability.hattened.vacuum")

	override fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) {
		val hat = HattenedHelper.getHatData(player)
		val createdItemAbilities = mutableListOf<ItemStackAbility>()
		val preexistingItemAbilities: List<ItemStackAbility> = hat.abilities.filter { it is ItemStackAbility }.map { it as ItemStackAbility } // final map is just to make the compiler happy

		world.getEntitiesByClass(ItemEntity::class.java, player.boundingBox.expand(6.0)) { it is ItemEntity }.forEach { itemEntity ->
			val stack = itemEntity.stack.copyAndEmpty()
			itemEntity.discard()

			for (ability in preexistingItemAbilities) {
				if (!ItemStack.areItemsAndComponentsEqual(stack, ability.stack))
					continue
				val transferAmount = min(ability.stack.maxCount - ability.stack.count, stack.count)
				stack.split(transferAmount)
				ability.markDirty(ItemStackAbility(ability.uuid, ability.stack.copyAndEmpty().apply { increment(transferAmount) }))
				if (stack.isEmpty)
					break
			}

			if (!stack.isEmpty)
				createdItemAbilities.add(ItemStackAbility(stack))
		}

		createdItemAbilities.forEach((player as ServerPlayerEntityMinterface)::`hattened$proposeAbility`)
	}

	companion object {
		val TYPE: AbilityType<VacuumAbility> = object : AbilityType<VacuumAbility>() {
			override val id = HattenedMain.id("vacuum")
			override val codec: MapCodec<VacuumAbility> = RecordCodecBuilder.mapCodec { builder -> commonCodec(builder).apply(builder, ::VacuumAbility) }
		}
	}
}