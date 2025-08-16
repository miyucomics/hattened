package miyucomics.hattened.misc

import miyucomics.hattened.structure.HatData
import miyucomics.hattened.structure.HatPose

interface HatDataSmuggler {
	fun setHat(hat: HatData)
	fun getHat(): HatData
	fun setPose(pose: HatPose)
	fun getPose(): HatPose
}