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
class AutoHasteScript: ScriptInterface {
    override fun execute(client: Client) {
        if( client.isHaste ) {
            return
        }

        if( client.hasEnemies() ) {
            return
        }

        client.castSpell(Spell.HASTE)
    }

    override fun type() = listOf(ScriptType.SUPPORT)
}