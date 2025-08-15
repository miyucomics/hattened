package miyucomics.hattened.misc

import miyucomics.hattened.structure.HatDataAttachment
import miyucomics.hattened.structure.HatPose

interface HatDataSmuggler {
	fun setHat(hat: HatDataAttachment)
	fun getHat(): HatDataAttachment
	fun setPose(pose: HatPose)
	fun getPose(): HatPose
}