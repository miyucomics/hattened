package miyucomics.hattened.misc

import miyucomics.hattened.attach.HatData
import miyucomics.hattened.attach.HatState

interface PlayerEntityRenderStateMinterface {
	fun setHat(hat: HatData)
	fun getHat(): HatData

	fun setHatState(state: HatState)
	fun getHatState(): HatState
}