package miyucomics.hattened.misc

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.util.Uuids
import java.util.*

class Card(val stack: ItemStack, var uuid: UUID) {
	constructor(stack: ItemStack) : this(stack, UUID.randomUUID())

	var mutated = false
	var replacement: Card? = null

	fun markDirty() {
		this.mutated = true
		this.replacement = if (this.stack.isEmpty) null else Card(this.stack, this.uuid)
	}

	companion object {
		val CODEC: Codec<Card> = RecordCodecBuilder.create { builder ->
			builder.group(
				ItemStack.CODEC.fieldOf("stack").forGetter(Card::stack),
				Uuids.CODEC.fieldOf("uuid").forGetter(Card::uuid),
			).apply(builder, ::Card)
		}
	}
}