package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.ScriptType
import ocr_bot.ScriptInterface.Spell
import ocr_bot.castSpell
import ocr_bot.hasEnemies

/**
 *  Utrzymywanie przyśpieszenia na postaci
 */
class AutoHasteScript(
    private val spell: Spell
): ScriptInterface {
    override fun name() = this::class.java.simpleName

    override fun execute(client: Client): Boolean {
        if( client.isHaste ) {
            return false
        }

        if( client.hasEnemies() ) {
            return false
        }

        return client.castSpell(spell)
    }

    override fun type() = listOf(ScriptType.SUPPORT)
}