package miyucomics.hattened.render

import miyucomics.hattened.misc.PlayerEntityRenderStateMinterface
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Arm
import net.minecraft.util.math.RotationAxis

class HatFeatureRenderer(context: FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel>) : FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel>(context) {
	private val hatModel = HatModel()

	override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, state: PlayerEntityRenderState, limbAngle: Float, limbDistance: Float) {
		matrices.push()
		if ((state as PlayerEntityRenderStateMinterface).isUsingHat()) {
			contextModel.leftArm.applyTransform(matrices)
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f))
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30.0f))
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-30.0f))
			matrices.scale(0.8f, 0.8f, 0.8f)
			matrices.translate(0.65f, -0.15f, -1.4f)
		} else {
			contextModel.head.applyTransform(matrices)
			matrices.translate(0.5f, -0.5f, -0.5f)
		}
		hatModel.render(matrices, vertexConsumers, light)
		matrices.pop()
	}
}