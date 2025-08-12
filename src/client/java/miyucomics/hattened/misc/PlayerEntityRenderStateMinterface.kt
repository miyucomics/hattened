package miyucomics.hattened.misc

import miyucomics.hattened.attach.HatData

interface PlayerEntityRenderStateMinterface {
	fun setHat(hat: HatData)
	fun getHat(): HatData
}