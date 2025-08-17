package miyucomics.hattened.misc

import miyucomics.hattened.misc.PeripheralManager.HAT_KEYBIND
import miyucomics.hattened.render.CardWidget
import miyucomics.hattened.structure.HatData
import miyucomics.hattened.structure.ServerCard
import net.minecraft.client.render.RenderTickCounter
import java.util.*
import kotlin.math.abs

object ClientStorage {
	var ticks = 0
	private var usingTime = 0
	var hat = HatData.DEFAULT
	val cards: HashMap<UUID, CardWidget> = HashMap()
	var orderedCards: List<Pair<CardWidget, Int>> = listOf()

	fun tick(hat: HatData) {
		this.hat = hat
		this.usingTime = (this.usingTime + if (HAT_KEYBIND.isPressed) 1 else -1).coerceIn(0, 10)

		hat.storage.forEachIndexed { index, card ->
			if (!cards.containsKey(card.uuid))
				cards[card.uuid] = CardWidget(index, card)
			cards[card.uuid]!!.card = card
			cards[card.uuid]!!.index = index
		}

		val hatUUIDs = hat.storage.map(ServerCard::uuid).toHashSet()
		val keysToRemove = mutableListOf<UUID>()
		cards.forEach { (uuid, card) ->
			if (!hatUUIDs.contains(uuid))
				card.removing = true
			if (card.canRemove())
				keysToRemove.add(uuid)
		}
		keysToRemove.forEach { cards.remove(it) }

		val cardCount = cards.size
		orderedCards = cards.values.map {
			var relativeIndex = it.index
			if (relativeIndex > cardCount / 2)
				relativeIndex -= cardCount
			it to relativeIndex
		}.sortedBy { -abs(it.second) }
	}

	@JvmStatic
	fun getProgress(tickCounter: RenderTickCounter): Float {
		if (!this.hat.hasHat)
			return 0f
		if (!PeripheralManager.shouldIntercept() && this.usingTime == 0)
			return 0f
		val raw = when (PeripheralManager.shouldIntercept()) {
			true -> this.usingTime + tickCounter.getTickProgress(false)
			false -> this.usingTime - tickCounter.getTickProgress(false)
		}
		return (raw / 10f).coerceIn(0f, 1f)
	}
}