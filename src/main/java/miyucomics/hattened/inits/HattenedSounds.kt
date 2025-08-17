package miyucomics.hattened.inits

import miyucomics.hattened.HattenedMain
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent

object HattenedSounds {
	lateinit var HAT_CONFETTI: SoundEvent
	lateinit var HAT_EQUIP: SoundEvent
	lateinit var INSERT_ITEM: SoundEvent
	lateinit var REMOVE_ITEM: SoundEvent
	lateinit var THROW_ITEM: SoundEvent

	fun init() {
		HAT_CONFETTI = register("item.hat.confetti")
		HAT_EQUIP = register("item.hat.equip")
		INSERT_ITEM = register("item.hat.insert")
		REMOVE_ITEM = register("item.hat.remove")
		THROW_ITEM = register("item.hat.throw")
	}

	fun register(name: String): SoundEvent {
		val id = HattenedMain.id(name)
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
	}
}