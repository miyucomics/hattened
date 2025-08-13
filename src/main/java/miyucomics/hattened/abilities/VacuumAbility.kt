package miyucomics.hattened.abilities

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.structure.Ability
import miyucomics.hattened.structure.AbilityType
import miyucomics.hattened.structure.HatPose
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import kotlin.jvm.java

class VacuumAbility() : Ability(TYPE) {
	constructor(active: Boolean) : this() {
		this.rightClickHeld = active
	}

	override fun getPose() = if (rightClickHeld) HatPose.Vacuuming else HatPose.SearchingHat
	override fun getTitle(): Text = Text.translatable("ability.hattened.vacuum")

	override fun onRightClickHold(world: ServerWorld, player: ServerPlayerEntity) {
		val playerPos = player.pos
		val lookVec = player.rotationVector

		world.getEntitiesByClass(Entity::class.java, player.boundingBox.expand(16.0)) { it is ItemEntity }.forEach { entity ->
			val toEntity = entity.pos.subtract(playerPos)
			if (toEntity.lengthSquared() <= 16.0 * 16.0) {
				val direction = toEntity.normalize()
				if (lookVec.dotProduct(direction) >= 0.8) {
					val motion = direction.multiply(-0.5)
					entity.velocity = entity.velocity.add(motion)
					entity.velocityModified = true
				}
			}
		}
	}

	companion object {
		var TYPE: AbilityType<VacuumAbility> = object : AbilityType<VacuumAbility>() {
			override val argc: Int = 0
			override val id = HattenedMain.id("vacuum")
			override val codec: MapCodec<VacuumAbility> = RecordCodecBuilder.mapCodec { it.group(Codec.BOOL.fieldOf("active").forGetter(VacuumAbility::rightClickHeld)).apply(it, ::VacuumAbility) }
		}
	}
}