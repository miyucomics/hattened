package miyucomics.hattened.misc

import miyucomics.hattened.attach.HatData
import net.minecraft.client.render.RenderTickCounter

object ClientStorage {
	var ticks = 0
	@JvmField
	var usingTime = 0
	var hat = HatData.DEFAULT

	fun updateClientHat(hat: HatData) {
		this.hat = hat
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