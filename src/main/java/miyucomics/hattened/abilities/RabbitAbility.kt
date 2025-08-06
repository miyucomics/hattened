package miyucomics.hattened.abilities

import miyucomics.hattened.structure.BaseAbility
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.GoatEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.entity.player.PlayerEntity

object RabbitAbility : BaseAbility("rabbit") {
	override fun onLeftClick(player: PlayerEntity) {
		if (!canUse())
			return

		player.world.spawnEntity(RabbitEntity(EntityType.RABBIT, player.world).apply {
			setPos(player.pos.x, player.pos.y, player.pos.z)
		})
		useCooldown(20)
	}

	override fun onRightClick(player: PlayerEntity) {
		if (!canUse())
			return

		player.world.spawnEntity(GoatEntity(EntityType.GOAT, player.world).apply {
			setPos(player.pos.x, player.pos.y, player.pos.z)
		})
		useCooldown(20)
	}
}