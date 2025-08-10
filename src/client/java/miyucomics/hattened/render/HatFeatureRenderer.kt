package miyucomics.hattened.render

import miyucomics.hattened.misc.PlayerEntityRenderStateMinterface
import miyucomics.hattened.misc.transformHat
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.math.MatrixStack

class HatFeatureRenderer(context: FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel>) : FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel>(context) {
	private val hatModel = HatModel()

	override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, state: PlayerEntityRenderState, limbAngle: Float, limbDistance: Float) {
		val hat = (state as PlayerEntityRenderStateMinterface).getHat()
		if (!hat.hasHat)
			return

		matrices.push()
		(state as PlayerEntityRenderStateMinterface).getHatState().getAbility().getPose().transformHat(matrices, contextModel)
		hatModel.render(matrices, vertexConsumers, light)
		matrices.pop()
	}
}