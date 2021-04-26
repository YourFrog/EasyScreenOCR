package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.Spell
import ocr_bot.castSpell

/**
 *  Skrypt do automatycznego siohania
 */
class AutoSioScript(
    /**
     *  Nazwa postaci którą należy utrzymać przy życiu
     */
    private val supportCreatureWithName: String
): ScriptInterface {
    override fun execute(client: Client) {
        if( client.isHealingCooldown ) {
            return
        }

        val needSio = client.friends.firstOrNull {
            it.name == supportCreatureWithName
        }?.let {
            when {
                client.enemies.isEmpty() && it.healthPercent < 75 -> true
                client.enemies.size == 1 && it.healthPercent < 75f -> true
                client.enemies.size in 2..3 && it.healthPercent < 85 -> true
                client.enemies.size > 3 && it.healthPercent < 90 -> true
                else -> false
            }
        } ?: false

        if( needSio ) {
            client.castSpell(Spell.SIO)
        }
    }

    override fun type() = listOf(ScriptInterface.ScriptType.HEALING)
}