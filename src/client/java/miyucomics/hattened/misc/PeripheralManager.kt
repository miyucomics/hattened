package miyucomics.hattened.misc

import miyucomics.hattened.networking.HatInputPayload
import miyucomics.hattened.networking.HatKeybindPayload
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object PeripheralManager {
	val HAT_KEYBIND = KeyBinding("key.hattened.hat", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.hattened")
	val DEQUIP_HAT_KEYBIND = KeyBinding("key.hattened.dequip", GLFW.GLFW_KEY_H, "key.categories.hattened")
	var previousState = false

	fun init() {
		KeyBindingHelper.registerKeyBinding(HAT_KEYBIND)
		KeyBindingHelper.registerKeyBinding(DEQUIP_HAT_KEYBIND)
	}

	fun tick() {
		if (DEQUIP_HAT_KEYBIND.isPressed)
			ClientPlayNetworking.send(HatKeybindPayload())

		if (!previousState && HAT_KEYBIND.isPressed) {
			ClientPlayNetworking.send(HatInputPayload(UserInput.LeftAltPressed))
		} else if (previousState && !HAT_KEYBIND.isPressed)
			ClientPlayNetworking.send(HatInputPayload(UserInput.LeftAltReleased))

		previousState = HAT_KEYBIND.isPressed
	}

	@JvmStatic
	fun onScroll(delta: Int) {
		when (delta) {
			-1 -> ClientPlayNetworking.send(HatInputPayload(UserInput.ScrollUp))
			1 -> ClientPlayNetworking.send(HatInputPayload(UserInput.ScrollDown))
		}
	}

	@JvmStatic
	fun onClick(button: Int, action: Int) {
		when (Pair(button, action)) {
			Pair(GLFW.GLFW_MOUSE_BUTTON_MIDDLE, GLFW.GLFW_PRESS) -> ClientPlayNetworking.send(HatInputPayload(UserInput.MiddleMousePressed))
			Pair(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS) -> ClientPlayNetworking.send(HatInputPayload(UserInput.LeftMousePressed))
			Pair(GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_PRESS) -> ClientPlayNetworking.send(HatInputPayload(UserInput.RightMousePressed))
			Pair(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE) -> ClientPlayNetworking.send(HatInputPayload(UserInput.LeftMouseReleased))
			Pair(GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_RELEASE) -> ClientPlayNetworking.send(HatInputPayload(UserInput.RightMouseReleased))
			else -> {}
		}
	}

	@JvmStatic
	fun shouldIntercept() = ClientStorage.hat.hasHat && HAT_KEYBIND.isPressed
}