package miyucomics.hattened.card_renderers

import miyucomics.hattened.abilities.ItemStackAbility
import miyucomics.hattened.render.CardRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

object ItemStackCardRenderer : CardRenderer<ItemStackAbility> {
	override fun render(context: DrawContext, ability: ItemStackAbility) {
		context.matrices.pushMatrix()
		context.matrices.scale(0.6f)
		context.matrices.translate(-8f, -8f)
		context.drawItem(ability.stack, 0, 0)
		context.drawStackOverlay(MinecraftClient.getInstance().textRenderer, ability.stack, 0, 0)
		context.matrices.popMatrix()
	}
}