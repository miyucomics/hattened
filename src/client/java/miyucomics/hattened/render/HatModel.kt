package miyucomics.hattened.render

import miyucomics.hattened.HattenedMain
import net.minecraft.client.model.*
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

class HatModel(root: ModelPart) : Model(root, { texture -> RenderLayer.getEntityCutout(texture) }) {
	val hat: ModelPart = root.getChild("hat")
	private val texture = HattenedMain.id("hat")

	constructor() : this(getTexturedModelData().createModel())

	fun render(matrices: MatrixStack, provider: VertexConsumerProvider, light: Int) {
		val consumer = provider.getBuffer(RenderLayer.getEntityCutoutNoCull(texture))
		hat.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV)
	}

	companion object {
		fun getTexturedModelData(): TexturedModelData {
			val modelData = ModelData()
			val root = modelData.root
			val hat = ModelPartBuilder.create()
				.uv(0, 0).cuboid(-6f, -9f, -6f, 12f, 1f, 12f)
				.uv(0, 13).cuboid(-4f, -17f, -4f, 8f, 8f, 8f)
				.uv(32, 13).cuboid(-4.5f, -11f, -4.5f, 9f, 1f, 9f)
			root.addChild("hat", hat, ModelTransform.NONE)
			return TexturedModelData.of(modelData, 32, 32)
		}
	}
}