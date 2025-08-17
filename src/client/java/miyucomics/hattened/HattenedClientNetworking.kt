package miyucomics.hattened

import miyucomics.hattened.inits.HattenedSounds
import miyucomics.hattened.networking.ConfettiPayload
import miyucomics.hattened.networking.SuckItemPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.particle.ItemPickupParticle
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import kotlin.random.Random

object HattenedClientNetworking {
	fun init() {
		ClientPlayNetworking.registerGlobalReceiver(ConfettiPayload.ID) { payload, context ->
			val random = Random(payload.seed)
			val pos = payload.position
			val dir = payload.direction
			val client = context.client()
			client.execute {
				client.world!!.playSoundClient(pos.x, pos.y, pos.z, HattenedSounds.HAT_CONFETTI, SoundCategory.MASTER, 1f, 1f, true)
				(0..99).forEach { _ ->
					val velocity = dir.add(
						random.nextDouble(-1.0, 1.0) / 5.0,
						random.nextDouble(-1.0, 1.0) / 5.0,
						random.nextDouble(-1.0, 1.0) / 5.0
					).multiply(2.0)
					client.world!!.addParticleClient(HattenedMain.CONFETTI_PARTICLE, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
				}
			}
		}

		ClientPlayNetworking.registerGlobalReceiver(SuckItemPayload.ID) { payload, context ->
			val world = context.player().clientWorld
			val item = world.getEntityById(payload.item)!! as ItemEntity
			val collector = world.getEntityById(payload.collector)!!
			context.client().execute {
				val client = MinecraftClient.getInstance()
				client.particleManager.addParticle(ItemPickupParticle(client.entityRenderDispatcher, world, item.copy().also { it.stack.copyWithCount(1) }, collector))
				world.playSoundClient(collector.x, collector.y, collector.z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (HattenedMain.RANDOM.nextFloat() - HattenedMain.RANDOM.nextFloat()) * 1.4F + 2.0F, false)
				if (!item.stack.isEmpty)
					item.stack.decrement(1)
				if (item.stack.isEmpty)
					world.removeEntity(item.id, Entity.RemovalReason.DISCARDED)
			}
		}
	}
}