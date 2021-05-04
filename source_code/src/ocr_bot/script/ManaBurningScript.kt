package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.ScriptType
import ocr_bot.ScriptInterface.Spell
import ocr_bot.castSpell
import java.awt.event.KeyEvent

/**
 *  Automatyczne spalanie many
 */
class ManaBurningScript(
    private val spell: Spell
): ScriptInterface {
    override fun name() = this::class.java.simpleName

    override fun execute(client: Client): Boolean {
        if( client.enemies.isNotEmpty() ) {
            return false
        }

        if( client.mana.percent() < 89 ) {
            return false
        }

        if( client.isHungry ) {
            return false
        }

        return client.castSpell(spell)
    }

    override fun type() = listOf(ScriptType.OTHER)
}