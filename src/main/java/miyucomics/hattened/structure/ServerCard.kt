package miyucomics.hattened.structure

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.util.Uuids
import java.util.*

class ServerCard(val stack: ItemStack, var uuid: UUID) {
	constructor(stack: ItemStack) : this(stack, UUID.randomUUID())

	var mutated = false
	var replacement: ServerCard? = null

	fun markDirty() {
		this.mutated = true
		this.replacement = if (this.stack.isEmpty) null else ServerCard(this.stack, this.uuid)
	}

	companion object {
		val CODEC: Codec<ServerCard> = RecordCodecBuilder.create { builder ->
			builder.group(
				ItemStack.CODEC.fieldOf("stack").forGetter(ServerCard::stack),
				Uuids.CODEC.fieldOf("uuid").forGetter(ServerCard::uuid),
			).apply(builder, ::ServerCard)
		}
	}
}