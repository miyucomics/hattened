package miyucomics.hattened.structure

import miyucomics.hattened.abilities.Ability

@Suppress("FunctionName")
interface ServerPlayerEntityMinterface {
	fun `hattened$queueUserInput`(input: UserInput)
	fun `hattened$proposeAbility`(ability: Ability)
}