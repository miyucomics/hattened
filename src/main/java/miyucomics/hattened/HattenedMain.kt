package miyucomics.hattened

import miyucomics.hattened.abilities.Ability
import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.inits.HattenedNetworking
import miyucomics.hattened.item.CardItem
import miyucomics.hattened.item.HatItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
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

	val CONFETTI_PARTICLE: SimpleParticleType = Registry.register(Registries.PARTICLE_TYPE, id("confetti"), FabricParticleTypes.simple(true))

	val ABILITY_COMPONENT: ComponentType<Ability> = Registry.register(Registries.DATA_COMPONENT_TYPE, id("ability"), ComponentType.builder<Ability>().codec(Ability.CODEC).build())
	val CARD_ITEM_KEY: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, id("card"))
	val CARD_ITEM: CardItem = Registry.register(Registries.ITEM, CARD_ITEM_KEY, CardItem)

	val ABILITIES_COMPONENT: ComponentType<List<Ability>> = Registry.register(Registries.DATA_COMPONENT_TYPE, id("abilities"), ComponentType.builder<List<Ability>>().codec(Ability.CODEC.listOf()).build())
	val HAT_ITEM_KEY: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, id("hat"))
	val HAT_ITEM: HatItem = Registry.register(Registries.ITEM, HAT_ITEM_KEY, HatItem)

	override fun onInitialize() {
		HattenedAbilities.init()
		HattenedAttachments.init()
		HattenedNetworking.init()

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register { it.add(HAT_ITEM) }
	}
}