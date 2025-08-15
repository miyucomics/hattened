package miyucomics.hattened.abilities

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.structure.HatPose
import miyucomics.hattened.structure.HattenedHelper
import net.minecraft.entity.ItemEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.*
import kotlin.math.min

class VacuumAbility(uuid: UUID) : Ability(TYPE, uuid) {
	override fun getPose() = if (rightClickHeld) HatPose.Vacuuming else HatPose.SearchingHat
	override fun getTitle(): Text = Text.translatable("ability.hattened.vacuum")

	override fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) {
		val hat = HattenedHelper.getHatData(player)
		val createdItemAbilities = mutableListOf<ItemStackAbility>()
		val preexistingItemAbilities: List<ItemStackAbility> = hat.abilities.filter { it is ItemStackAbility }.map { it as ItemStackAbility } // final map is just to make the compiler happy

		world.getEntitiesByClass(ItemEntity::class.java, player.boundingBox.expand(6.0)) { it is ItemEntity }.forEach { itemEntity ->
			val stack = itemEntity.stack.copyAndEmpty()
			itemEntity.discard()

			for (ability in preexistingItemAbilities) {
				val transferAmount = min(ability.stack.maxCount - ability.stack.count, stack.count)
				stack.split(transferAmount)
				ability.stack.increment(transferAmount)
				if (stack.isEmpty)
					break
			}
			if (!stack.isEmpty)
				createdItemAbilities.add(ItemStackAbility(stack))
		}

		HattenedHelper.setHatData(player, hat.copy(abilities = hat.abilities.plus(createdItemAbilities)))
	}

	companion object {
		val TYPE: AbilityType<VacuumAbility> = object : AbilityType<VacuumAbility>() {
			override val id = HattenedMain.id("vacuum")
			override val codec: MapCodec<VacuumAbility> = RecordCodecBuilder.mapCodec { builder -> commonCodec(builder).apply(builder, ::VacuumAbility) }
		}
	}
}