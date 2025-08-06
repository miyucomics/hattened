package miyucomics.hattened.structure

import dev.kosmx.playerAnim.api.PartKey
import dev.kosmx.playerAnim.core.util.Vec3f
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.RotationAxis
import kotlin.math.cos
import kotlin.math.sin

enum class HatPose(val transformHat: (MatrixStack, PlayerEntityModel) -> Unit, val transformBody: ((modelKey: PartKey, time: Float) -> Vec3f?) = { _, _ -> null }) {
	OnHead(transformHat = { matrices, contextModel ->
		contextModel.head.applyTransform(matrices)
		matrices.translate(0.5f, -0.5f, -0.5f)
	}),

	SearchingHat(transformHat = { matrices, contextModel ->
		contextModel.leftArm.applyTransform(matrices)
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f))
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30.0f))
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-30.0f))
		matrices.scale(0.8f, 0.8f, 0.8f)
		matrices.translate(0.65f, -0.15f, -1.4f)
	}, transformBody = { key, time ->
		when (key) {
			PartKey.LEFT_ARM  -> Vec3f(-50f, 45f, 0f)
			PartKey.RIGHT_ARM -> {
				val pitch = sin(time / 3f) * 2f
				val yaw = cos(time / 2f) * 2f
				Vec3f(-50f + pitch, -25f + yaw, 0f)
			}
			else -> null
		}
	}),

	Vacuuming(transformHat = { matrices, contextModel ->
		matrices.translate(0.5f, 0f, -0.8f)
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
	}, transformBody = { key, _ ->
		when (key) {
			PartKey.LEFT_ARM -> Vec3f(-50f, 10f, 0f)
			PartKey.RIGHT_ARM -> Vec3f(-50f, -10f, 0f)
			else -> null
		}
	}),

	Bowing(transformHat = { matrices, contextModel ->
		contextModel.rightArm.applyTransform(matrices)
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f))
		matrices.translate(0.65f, 0.05f, -1.4f)
	}, transformBody = { key, _ ->
		when (key) {
			PartKey.LEFT_ARM -> Vec3f(0f, -90f, -135f)
			PartKey.RIGHT_ARM -> Vec3f(0f, 90f, 135f)
			else -> null
		}
	})
}