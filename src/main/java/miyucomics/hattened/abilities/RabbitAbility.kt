package miyucomics.hattened.abilities

import miyucomics.hattened.structure.BaseAbility
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.GoatEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.server.network.ServerPlayerEntity

object RabbitAbility : BaseAbility("rabbit") {
	override fun onLeftClick(player: ServerPlayerEntity) {
		player.world.spawnEntity(RabbitEntity(EntityType.RABBIT, player.world).apply {
			setPos(player.pos.x, player.pos.y, player.pos.z)
		})
	}

	override fun onRightClick(player: ServerPlayerEntity) {
		player.world.spawnEntity(GoatEntity(EntityType.GOAT, player.world).apply {
			setPos(player.pos.x, player.pos.y, player.pos.z)
		})
	}
}