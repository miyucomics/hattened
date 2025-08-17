package miyucomics.hattened.render

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.Ability
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

interface CardRenderer<T : Ability> {
	fun getCover(): Identifier = HattenedMain.id("textures/cards/base.png")
	fun render(context: DrawContext, ability: T) {}
}