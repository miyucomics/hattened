package miyucomics.hattened

import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import miyucomics.hattened.misc.ClientStorage
import miyucomics.hattened.misc.ConfettiParticle
import miyucomics.hattened.misc.PeripheralManager
import miyucomics.hattened.render.HatPlayerModel
import miyucomics.hattened.misc.HattenedHelper
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry

object HattenedClient : ClientModInitializer {
	override fun onInitializeClient() {
		HattenedClientNetworking.init()
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
			ClientStorage.ticks += 1
			ClientStorage.tick(HattenedHelper.getHatData(client.player!!))
		}
	}
}