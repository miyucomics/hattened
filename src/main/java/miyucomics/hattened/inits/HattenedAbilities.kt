package miyucomics.hattened.inits

import miyucomics.hattened.HattenedMain
import miyucomics.hattened.abilities.RabbitAbility
import miyucomics.hattened.abilities.VacuumAbility
import miyucomics.hattened.structure.Ability
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry

object HattenedAbilities {
	private val ABILITY_KEY: RegistryKey<Registry<Ability>> = RegistryKey.ofRegistry(HattenedMain.id("ability"))
	val ABILITY_REGISTRY: SimpleRegistry<Ability> = FabricRegistryBuilder.createSimple(ABILITY_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister()

	fun init() {
		Registry.register(ABILITY_REGISTRY, HattenedMain.id("rabbit"), RabbitAbility)
		Registry.register(ABILITY_REGISTRY, HattenedMain.id("apple"), VacuumAbility(ItemStack(Items.APPLE)))
		Registry.register(ABILITY_REGISTRY, HattenedMain.id("fruit"), VacuumAbility(ItemStack(Items.WATER_BUCKET)))
		Registry.register(ABILITY_REGISTRY, HattenedMain.id("tree"), VacuumAbility(ItemStack(Items.ACACIA_SAPLING)))
		Registry.register(ABILITY_REGISTRY, HattenedMain.id("vacuum"), VacuumAbility(ItemStack(Items.WOODEN_SWORD)))
	}
}