package miyucomics.hattened.render

import miyucomics.hattened.misc.PlayerEntityRenderStateMinterface
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.RotationAxis

class HatFeatureRenderer(context: FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel>) : FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel>(context) {
	private val hatModel = HatModel()

	override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, state: PlayerEntityRenderState, limbAngle: Float, limbDistance: Float) {
		matrices.push()
		if ((state as PlayerEntityRenderStateMinterface).isUsingHat()) {
			contextModel.leftArm.applyTransform(matrices)
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f))
			matrices.translate(0f, 0.6f, -0.6f)
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(20.0f))
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(10.0f))
		} else
			contextModel.head.applyTransform(matrices)
		hatModel.render(matrices, vertexConsumers, light)
		matrices.pop()
	}
}