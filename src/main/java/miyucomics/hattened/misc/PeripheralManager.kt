package miyucomics.hattened.misc

import miyucomics.hattened.networking.HatControlPayload
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object PeripheralManager {
	val HAT_KEYBIND = KeyBinding("key.hattened.hat", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.hattened")
	var previousState = false

	fun init() {
		KeyBindingHelper.registerKeyBinding(HAT_KEYBIND)
	}

	fun tick() {
		ClientStorage.usingTime = (ClientStorage.usingTime + if (HAT_KEYBIND.isPressed) 1 else -1).coerceIn(0, 10)

		if (!previousState && HAT_KEYBIND.isPressed)
			ClientPlayNetworking.send(HatControlPayload(HatPose.BOWING))
		else if (previousState && !HAT_KEYBIND.isPressed)
			ClientPlayNetworking.send(HatControlPayload(HatPose.ON_HEAD))

		previousState = HAT_KEYBIND.isPressed
	}

	@JvmStatic
	fun onScroll(delta: Int) {

	}

	@JvmStatic
	fun onClick(button: Int, action: Int) {

	}

	@JvmStatic
	fun shouldIntercept() = HAT_KEYBIND.isPressed
}