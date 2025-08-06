package miyucomics.hattened.misc

import miyucomics.hattened.attach.HatState
import net.minecraft.client.render.RenderTickCounter

object ClientStorage {
	var ticks = 0
	@JvmField
	var usingTime = 0
	var animatedAbilityIndex = 0f

	fun tickMenu(state: HatState) {
		val speed = 0.2f
		val diff = state.selectedAbilityIndex - animatedAbilityIndex
		animatedAbilityIndex += diff * speed
	}

	@JvmStatic
	fun getSmoothUsingTime(tickCounter: RenderTickCounter): Float {
		val raw = when (PeripheralManager.shouldIntercept()) {
			true -> usingTime + tickCounter.getTickProgress(false)
			false -> usingTime - tickCounter.getTickProgress(false)
		}
		return (raw / 10f).coerceIn(0f, 1f)
	}
}