package miyucomics.hattened

import miyucomics.hattened.inits.HattenedAttachments
import miyucomics.hattened.inits.HattenedNetworking
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object HattenedMain : ModInitializer {
	fun id(path: String): Identifier = Identifier.of("hattened", path)

	override fun onInitialize() {
		HattenedAttachments.init()
		HattenedNetworking.init()
	}
}