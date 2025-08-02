package miyucomics.hattened.render

import miyucomics.hattened.HattenedMain
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class HatFeatureRenderer(context: FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel>) : FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel>(context) {
	private val hatModel = HatModel(HatModel.getTexturedModelData().createModel())
	private val texture = HattenedMain.id("hat")

	override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, state: PlayerEntityRenderState, limbAngle: Float, limbDistance: Float) {
		if (state.invisible)
			return

		matrices.push()
		contextModel.head.applyTransform(matrices)
		val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture))
		hatModel.hat.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV)
		matrices.pop()
	}
}