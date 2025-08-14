package miyucomics.hattened.misc

import miyucomics.hattened.attach.HatDataAttachment
import miyucomics.hattened.structure.HatPose

interface PlayerEntityRenderStateMinterface {
	fun setHat(hat: HatDataAttachment)
	fun getHat(): HatDataAttachment
	fun setPose(pose: HatPose)
	fun getPose(): HatPose
}