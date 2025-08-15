package miyucomics.hattened.render

import miyucomics.hattened.abilities.Ability
import net.minecraft.client.gui.DrawContext

interface CardRenderer<T : Ability> {
	fun render(context: DrawContext, ability: T)
}