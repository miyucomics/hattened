package miyucomics.hattened.misc

import miyucomics.hattened.HattenedClient.HAT_KEYBIND
import miyucomics.hattened.networking.HatControlPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object PeripheralManager {
	var previousState = false

	fun tick() {
		ClientStorage.usingTime = (ClientStorage.usingTime + if (HAT_KEYBIND.isPressed) 1 else -1).coerceIn(0, 10)

		if (!previousState && HAT_KEYBIND.isPressed)
			ClientPlayNetworking.send(HatControlPayload(true))
		else if (previousState && !HAT_KEYBIND.isPressed)
			ClientPlayNetworking.send(HatControlPayload(false))

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