package miyucomics.hattened.abilities

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedHelper
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.structure.HatPose
import net.minecraft.command.argument.EntityArgumentType.entity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.*

class VacuumAbility(uuid: UUID) : Ability(TYPE, uuid) {
	override fun getPose() = if (rightClickHeld) HatPose.Vacuuming else HatPose.SearchingHat
	override fun getTitle(): Text = Text.translatable("ability.hattened.vacuum")

	override fun onRightClick(world: ServerWorld, player: ServerPlayerEntity) {
		val hat = HattenedHelper.getHatData(player)
		val newAbilities = world.getEntitiesByClass(Entity::class.java, player.boundingBox.expand(6.0)) { it is ItemEntity }.map { ItemStackAbility((it as ItemEntity).stack.copyAndEmpty()) }
		HattenedHelper.setHatData(player, hat.copy(abilities = hat.abilities.plus(newAbilities)))
	}

	companion object {
		var TYPE: AbilityType<VacuumAbility> = object : AbilityType<VacuumAbility>() {
			override val id = HattenedMain.id("vacuum")
			override val codec: MapCodec<VacuumAbility> = RecordCodecBuilder.mapCodec { builder -> commonCodec(builder).apply(builder, ::VacuumAbility) }
		}
	}
}