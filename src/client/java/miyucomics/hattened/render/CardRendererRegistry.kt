package miyucomics.hattened.render

import com.mojang.serialization.Lifecycle
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.Ability
import miyucomics.hattened.abilities.AbilityType
import miyucomics.hattened.abilities.ItemStackAbility
import miyucomics.hattened.card_renderers.ItemStackCardRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry

object CardRendererRegistry {
	val ABILITY_RENDERER_REGISTRY: SimpleRegistry<CardRenderer<*>> = SimpleRegistry(RegistryKey.ofRegistry(HattenedMain.id("ability_renderer")), Lifecycle.stable())

	fun init() {
		register(ItemStackAbility.TYPE, ItemStackCardRenderer)
	}

	fun <T : Ability> register(type: AbilityType<T>, renderer: CardRenderer<T>) {
		Registry.register(ABILITY_RENDERER_REGISTRY, type.id, renderer)
	}

	fun <T: Ability> render(context: DrawContext, ability: T) {
		val renderer = ABILITY_RENDERER_REGISTRY.get(ability.type.id)
		if (renderer is CardRenderer<*>)
			(renderer as? CardRenderer<T>)?.render(context, ability)
	}
}