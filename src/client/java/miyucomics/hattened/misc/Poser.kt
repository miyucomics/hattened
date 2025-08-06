package miyucomics.hattened.misc

import dev.kosmx.playerAnim.api.PartKey
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hattened.structure.HatPose
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.RotationAxis
import kotlin.math.cos
import kotlin.math.sin

fun HatPose.transformHat(matrices: MatrixStack, contextModel: PlayerEntityModel) = when (this) {
	HatPose.OnHead -> {
		contextModel.head.applyTransform(matrices)
		matrices.translate(0.5f, -0.5f, -0.5f)
	}
	HatPose.SearchingHat -> {
		contextModel.leftArm.applyTransform(matrices)
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30.0f))
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-30.0f))
		matrices.scale(0.8f, 0.8f, 0.8f)
		matrices.translate(0.65f, -0.15f, -1.4f)
	}
	HatPose.Vacuuming -> {
		matrices.translate(0.5f, 0f, -0.8f)
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
	}
	HatPose.Bowing -> {
		contextModel.rightArm.applyTransform(matrices)
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f))
		matrices.translate(0.65f, 0.05f, -1.4f)
	}
}

fun HatPose.rotateBody(key: PartKey, time: Float): Vec3f? = when (this) {
	HatPose.OnHead -> null
	HatPose.SearchingHat -> when (key) {
		PartKey.LEFT_ARM  -> Vec3f(-50f, 45f, 0f)
		PartKey.RIGHT_ARM -> {
			val pitch = sin(time / 3f) * 2f
			val yaw = cos(time / 2f) * 2f
			Vec3f(-50f + pitch, -25f + yaw, 0f)
		}
		else -> null
	}
	HatPose.Vacuuming -> when (key) {
		PartKey.LEFT_ARM -> Vec3f(-50f, 10f, 0f)
		PartKey.RIGHT_ARM -> Vec3f(-50f, -10f, 0f)
		else -> null
	}
	HatPose.Bowing -> when (key) {
		PartKey.LEFT_ARM -> Vec3f(0f, -90f, -135f)
		PartKey.RIGHT_ARM -> Vec3f(0f, 90f, 135f)
		else -> null
	}
}