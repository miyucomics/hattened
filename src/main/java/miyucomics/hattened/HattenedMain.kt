package miyucomics.hattened

import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.inits.HattenedNetworking
import miyucomics.hattened.item.HatItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.component.ComponentType
import net.minecraft.item.ItemGroups
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.util.*

object HattenedMain : ModInitializer {
	val RANDOM = Random()
	fun id(path: String): Identifier = Identifier.of("hattened", path)

	val HAT_ITEM: HatItem = Registry.register(Registries.ITEM, id("hat"), HatItem)
	val CONFETTI_PARTICLE: SimpleParticleType = Registry.register(Registries.PARTICLE_TYPE, id("confetti"), FabricParticleTypes.simple(true))
	val ABILITY_COMPONENT: ComponentType<List<Identifier>> = Registry.register(Registries.DATA_COMPONENT_TYPE, id("abilities"), ComponentType.builder<List<Identifier>>().codec(Identifier.CODEC.listOf()).build())

	override fun onInitialize() {
		HattenedAbilities.init()
		HattenedAttachments.init()
		HattenedNetworking.init()
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register { it.add(HAT_ITEM) }
	}
}