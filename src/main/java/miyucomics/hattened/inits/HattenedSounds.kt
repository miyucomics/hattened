package miyucomics.hattened.inits

import miyucomics.hattened.HattenedMain
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent

object HattenedSounds {
	val HAT_EQUIP = register("item.hat.equip")
	val INSERT_ITEM = register("item.hat.insert")
	val REMOVE_ITEM = register("item.hat.remove")
	val THROW_ITEM = register("item.hat.throw")

	fun init() {}

	fun register(name: String): SoundEvent {
		val id = HattenedMain.id(name)
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
	}
}