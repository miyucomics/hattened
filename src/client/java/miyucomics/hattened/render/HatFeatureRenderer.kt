package miyucomics.hattened.render

import miyucomics.hattened.misc.HatDataSmuggler
import miyucomics.hattened.misc.Poser
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.math.MatrixStack

class HatFeatureRenderer(context: FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel>) : FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel>(context) {
	private val hatModel = HatModel()

	override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, state: PlayerEntityRenderState, limbAngle: Float, limbDistance: Float) {
		val hat = (state as HatDataSmuggler).getHat()
		if (!hat.hasHat)
			return

		matrices.push()
		Poser.transformHat((state as HatDataSmuggler).getPose(), matrices, contextModel)
		hatModel.render(matrices, vertexConsumers, light)
		matrices.pop()
	}
}