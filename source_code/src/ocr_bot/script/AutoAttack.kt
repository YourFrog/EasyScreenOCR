package ocr_bot.script

import ocr_bot.*
import ocr_bot.ScriptInterface.ScriptType

/**
 *  Skrypt automatycznie atakujący potwory
 */
class AutoAttack(
    val ignoreMonsters: List<ScriptInterface.Monster> = emptyList()
): ScriptInterface {
    override fun execute(client: Client) {
        if( client.isAttackCooldown ) {
            return
        }

        if( client.noHasTarget() ) {
            return
        }

        client.getTargetCreature()?.let { creature ->
            when(client.enemies.size) {
                1 -> attackSingleCreature(client, creature)
                else -> attackMultipleCreature(client, creature)
            }
        }
    }

    override fun type() = listOf(ScriptType.ATTACK)

    /**
     *  Zaatakowanie pojedyńczego potwora
     */
    private fun attackSingleCreature(client: Client, creature: Creature) {
        val monster = ScriptInterface.Monster.from(creature.name)

        if( ignoreMonsters.contains(monster) ) {
            return
        }

        val item = when(monster) {
            ScriptInterface.Monster.FRAZZLEMAW -> ScriptInterface.Item.RUNE_SD
            ScriptInterface.Monster.SILENCER -> ScriptInterface.Item.RUNE_ICYCLE
            ScriptInterface.Monster.GRIMELEECH -> ScriptInterface.Item.RUNE_ICYCLE
            ScriptInterface.Monster.LIZARD_CHOSEN -> ScriptInterface.Item.RUNE_SD
            ScriptInterface.Monster.DEMON -> ScriptInterface.Item.RUNE_SD
            ScriptInterface.Monster.VEXCLAW -> ScriptInterface.Item.RUNE_SD
            else -> {
                System.out.println("Unknown creature: " + creature.name)
                ScriptInterface.Item.RUNE_SD
            }
        }

        client.useItem(item)
    }

    /**
     *  Próba zaatakowania wielu potworów
     */
    private fun attackMultipleCreature(client: Client, creature: Creature) {
        val item = ScriptInterface.Item.RUNE_AVALANCHE

        client.useItem(item)
    }
}