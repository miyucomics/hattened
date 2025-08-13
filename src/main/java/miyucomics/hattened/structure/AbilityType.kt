package miyucomics.hattened.structure

import com.mojang.serialization.MapCodec
import net.minecraft.util.Identifier

abstract class AbilityType<T : Ability> {
	abstract val argc: Int
	abstract val id: Identifier
	abstract val codec: MapCodec<T>
}