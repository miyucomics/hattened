package miyucomics.hattened.inits

import com.mojang.serialization.Lifecycle
import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.ConfettiAbility
import miyucomics.hattened.abilities.ItemStackAbility
import miyucomics.hattened.structure.AbilityType
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry

object HattenedAbilities {
	val ABILITY_REGISTRY: SimpleRegistry<AbilityType<*>> = SimpleRegistry(RegistryKey.ofRegistry(HattenedMain.id("ability")), Lifecycle.stable())

	fun init() {
		register(ConfettiAbility.TYPE)
		register(ItemStackAbility.TYPE)
	}

	fun register(type: AbilityType<*>) {
		Registry.register(ABILITY_REGISTRY, type.id, type)
	}
}