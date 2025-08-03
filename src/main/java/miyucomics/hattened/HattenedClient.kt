package miyucomics.hattened

import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import dev.kosmx.playerAnim.minecraftApi.layers.LeftHandedHelperModifier
import miyucomics.hattened.misc.HatData
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.misc.ClientStorage
import miyucomics.hattened.render.HatPlayerModel
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object HattenedClient : ClientModInitializer {
	val HAT_KEYBIND = KeyBinding("key.hattened.hat", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.hattened")

	override fun onInitializeClient() {
		PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register { player, stack ->
			val animation = ModifierLayer(HatPlayerModel(player))
			PlayerAnimationAccess.getPlayerAssociatedData(player).set(HattenedMain.id("hat"), animation)
			stack.addAnimLayer(10000, animation)
		}

		ClientTickEvents.END_CLIENT_TICK.register { client ->
			if (client.player == null)
				return@register

			ClientStorage.ticks += 1
			client.player!!.setAttached(HattenedAttachments.HAT_DATA, HatData(true, HAT_KEYBIND.isPressed))
		}
	}
}