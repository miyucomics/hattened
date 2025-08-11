package miyucomics.hattened

import miyucomics.hattened.inits.HattenedAbilities
import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.inits.HattenedNetworking
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.util.Random

object HattenedMain : ModInitializer {
	fun id(path: String): Identifier = Identifier.of("hattened", path)
	val CONFETTI_PARTICLE: SimpleParticleType = FabricParticleTypes.simple(true)
	val RANDOM = Random()

	override fun onInitialize() {
		HattenedAbilities.init()
		HattenedAttachments.init()
		HattenedNetworking.init()

		Registry.register(Registries.PARTICLE_TYPE, id("confetti"), CONFETTI_PARTICLE)
	}
}