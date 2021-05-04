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
    override fun name() = this::class.java.simpleName

    override fun execute(client: Client): Boolean {
        if( client.isHealingCooldown ) {
            return false
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
            return client.castSpell(Spell.SIO)
        }

        return false
    }

    override fun type() = listOf(ScriptInterface.ScriptType.HEALING)
}