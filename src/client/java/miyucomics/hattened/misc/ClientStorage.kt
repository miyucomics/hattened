package miyucomics.hattened.misc

import miyucomics.hattened.abilities.Ability
import miyucomics.hattened.misc.PeripheralManager.HAT_KEYBIND
import miyucomics.hattened.render.Card
import miyucomics.hattened.structure.HatData
import net.minecraft.client.render.RenderTickCounter
import java.util.*
import kotlin.math.abs

object ClientStorage {
	var ticks = 0
	private var usingTime = 0
	var hat = HatData.DEFAULT
	val cards: HashMap<UUID, Card> = HashMap()
	var orderedCards: List<Pair<Card, Int>> = listOf()

	fun tick(hat: HatData) {
		this.hat = hat
		this.usingTime = (this.usingTime + if (HAT_KEYBIND.isPressed) 1 else -1).coerceIn(0, 10)

		hat.abilities.forEachIndexed { index, ability ->
			if (!cards.containsKey(ability.uuid))
				cards[ability.uuid] = Card(index, ability)
			cards[ability.uuid]!!.ability = ability
			cards[ability.uuid]!!.index = index
		}

		val hatUUIDs = hat.abilities.map(Ability::uuid).toHashSet()
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