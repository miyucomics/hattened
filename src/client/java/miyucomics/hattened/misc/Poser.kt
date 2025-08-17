package miyucomics.hattened.misc

import dev.kosmx.playerAnim.api.PartKey
import dev.kosmx.playerAnim.core.util.MathHelper.lerp
import dev.kosmx.playerAnim.core.util.Vec3f
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.RotationAxis
import kotlin.math.cos
import kotlin.math.sin

object Poser {
	fun transformHat(pose: HatPose, matrices: MatrixStack, contextModel: PlayerEntityModel) {
		val time = ClientStorage.ticks + MinecraftClient.getInstance().renderTickCounter.getTickProgress(false)
		return when (pose) {
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
				matrices.translate(0.5f + sin(time / 5f) / 200, -cos(time / 6f + 1f) / 200, -0.8f)
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
			}
		}
	}

	fun rotateBody(pose: HatPose, key: PartKey, tickDelta: Float, player: PlayerEntity): Vec3f? {
		val time = ClientStorage.ticks + tickDelta
		return when (pose) {
			HatPose.OnHead -> null
			HatPose.SearchingHat -> when (key) {
				PartKey.LEFT_ARM  -> Vec3f(-50f, 45f, 0f)
				PartKey.RIGHT_ARM -> {
					if (player.handSwinging) {
						Vec3f(lerp(player.getHandSwingProgress(tickDelta), -50f, -110f), lerp(player.getHandSwingProgress(tickDelta), -25f, -20f), 0f)
					} else {
						val pitch = sin(time / 3f) * 2f
						val yaw = cos(time / 2f) * 2f
						Vec3f(-50f + pitch, -25f + yaw, 0f)
					}
				}
				else -> null
			}
			HatPose.Vacuuming -> when (key) {
				PartKey.LEFT_ARM -> Vec3f(-50f + sin(time / 5f) / 2, 10f + cos(time / 6f + 1f), 0f)
				PartKey.RIGHT_ARM -> Vec3f(-50f + sin(time / 5f) / 2, -10f + cos(time / 6f + 1f), 0f)
				else -> null
			}
		}
	}
}