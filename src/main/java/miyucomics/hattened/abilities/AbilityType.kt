package miyucomics.hattened.abilities

import com.mojang.datafixers.Products
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.Identifier
import net.minecraft.util.Uuids
import java.util.*

abstract class AbilityType<T : Ability> {
	abstract val argc: Int
	abstract val id: Identifier
	abstract val codec: MapCodec<T>

	companion object {
		fun <T: Ability> commonCodec(instance: RecordCodecBuilder.Instance<T>): Products.P1<RecordCodecBuilder.Mu<T>, UUID> =
			instance.group(Uuids.CODEC.fieldOf("uuid").forGetter(Ability::uuid))
	}
}