package miyucomics.hattened

import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.inits.HattenedNetworking
import miyucomics.hattened.inits.HattenedSounds
import miyucomics.hattened.item.HatItem
import miyucomics.hattened.structure.ServerCard
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

	val HAT_STORAGE_COMPONENT: ComponentType<List<ServerCard>> = Registry.register(Registries.DATA_COMPONENT_TYPE, id("hat_storage"), ComponentType.builder<List<ServerCard>>().codec(ServerCard.CODEC.listOf()).build())
	val HAT_ITEM_KEY: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, id("hat"))
	val HAT_ITEM: HatItem = Registry.register(Registries.ITEM, HAT_ITEM_KEY, HatItem)

	override fun onInitialize() {
		HattenedAttachments.init()
		HattenedNetworking.init()
		HattenedSounds.init()

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register { it.add(HAT_ITEM) }
	}
}