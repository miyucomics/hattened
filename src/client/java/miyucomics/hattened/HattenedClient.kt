package miyucomics.hattened

import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import miyucomics.hattened.misc.ClientStorage
import miyucomics.hattened.misc.ConfettiParticle
import miyucomics.hattened.misc.PeripheralManager
import miyucomics.hattened.networking.ConfettiPayload
import miyucomics.hattened.render.HatPlayerModel
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import kotlin.random.Random

object HattenedClient : ClientModInitializer {
	override fun onInitializeClient() {
		PeripheralManager.init()
		ParticleFactoryRegistry.getInstance().register(HattenedMain.CONFETTI_PARTICLE, ConfettiParticle::Factory)

		PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register { player, stack ->
			val animation = ModifierLayer(HatPlayerModel(player))
			PlayerAnimationAccess.getPlayerAssociatedData(player).set(HattenedMain.id("hat"), animation)
			stack.addAnimLayer(10000, animation)
		}

		ClientTickEvents.END_CLIENT_TICK.register { client ->
			if (client.player == null)
				return@register
			PeripheralManager.tick()
			ClientStorage.tickMenu(HattenedHelper.getHatData(client.player!!))
			ClientStorage.ticks += 1
		}

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