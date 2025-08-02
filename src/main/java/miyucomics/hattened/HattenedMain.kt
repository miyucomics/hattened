package miyucomics.hattened

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object HattenedMain : ModInitializer {
    @JvmField
	val LOGGER: Logger = LoggerFactory.getLogger("hattened")
	fun id(path: String) = Identifier.of("hattened", path)

	override fun onInitialize() {

	}
}