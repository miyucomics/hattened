package miyucomics.hattened.render

import miyucomics.hattened.structure.HatPose
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
		val hat = (state as PlayerEntityRenderStateMinterface).getHat()
		if (!hat.hasHat)
			return

		matrices.push()
		(state as PlayerEntityRenderStateMinterface).getHatState().hatPose.transformHat(matrices, contextModel)
		hatModel.render(matrices, vertexConsumers, light)
		matrices.pop()
	}
}