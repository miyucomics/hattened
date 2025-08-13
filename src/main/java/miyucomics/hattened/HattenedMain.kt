package miyucomics.hattened

import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.inits.HattenedNetworking
import miyucomics.hattened.item.HatItem
import miyucomics.hattened.structure.Ability
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemGroups
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.*

object HattenedMain : ModInitializer {
	val RANDOM = Random()
	fun id(path: String): Identifier = Identifier.of("hattened", path)

	val ABILITY_COMPONENT: ComponentType<List<Ability>> = Registry.register(Registries.DATA_COMPONENT_TYPE, id("abilities"), ComponentType.builder<List<Ability>>().codec(Ability.CODEC.listOf()).build())

	val HAT_ITEM = register("hat", ::HatItem, Settings().maxCount(1).component(ABILITY_COMPONENT, listOf()))
	val CONFETTI_PARTICLE: SimpleParticleType = Registry.register(Registries.PARTICLE_TYPE, id("confetti"), FabricParticleTypes.simple(true))

	override fun onInitialize() {
		HattenedAbilities.init()
		HattenedAttachments.init()
		HattenedNetworking.init()
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register { it.add(HAT_ITEM) }
	}

	private fun register(name: String, itemFactory: (Settings) -> Item, settings: Settings): Item {
		val itemKey = RegistryKey.of(RegistryKeys.ITEM, id(name))
		val item = itemFactory(settings.registryKey(itemKey))
		Registry.register(Registries.ITEM, itemKey, item)
		return item
	}
}