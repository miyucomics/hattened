package miyucomics.hattened

import miyucomics.hattened.networking.ConfettiPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
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
				client.world!!.playSoundClient(pos.x, pos.y, pos.z, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 1f, 1f, true)
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
	}
}