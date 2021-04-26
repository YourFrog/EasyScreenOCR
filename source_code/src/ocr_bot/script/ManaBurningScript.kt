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
    override fun execute(client: Client) {
        if( client.enemies.isNotEmpty() ) {
            return
        }

        if( client.mana.percent() < 99 ) {
            return
        }

        if( client.isHungry ) {
            return
        }

        client.castSpell(spell)
    }

    override fun type() = listOf(ScriptType.OTHER)
}