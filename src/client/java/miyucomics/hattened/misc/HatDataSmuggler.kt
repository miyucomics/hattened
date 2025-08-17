package miyucomics.hattened.misc

interface HatDataSmuggler {
	fun setHat(hat: HatData)
	fun getHat(): HatData
	fun setPose(pose: HatPose)
	fun getPose(): HatPose
}