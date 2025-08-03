package miyucomics.hattened.render

import miyucomics.hattened.HattenedMain
import net.minecraft.client.model.*
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack


class HatModel(root: ModelPart) : Model(root, { texture -> RenderLayer.getEntityCutout(texture) }) {
	val hat: ModelPart = root.getChild("hat")

	constructor() : this(getTexturedModelData().createModel())

	fun render(matrices: MatrixStack, provider: VertexConsumerProvider, light: Int) {
		val consumer = provider.getBuffer(RenderLayer.getEntityCutout(texture))
		hat.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV)
	}

	companion object {
		private val texture = HattenedMain.id("textures/hat.png")

		fun getTexturedModelData(): TexturedModelData {
			val modelData = ModelData()
			val root = modelData.root
			val hat = ModelPartBuilder.create()
				.uv(0, 0).cuboid(-13.5f, -2f, 2.5f, 11f, 2f, 11f)
				.uv(0, 13).cuboid(-12f, -10f, 4f, 8f, 8f, 8f)
			root.addChild("hat", hat, ModelTransform.NONE)
			return TexturedModelData.of(modelData, 64, 64)
		}
	}
}