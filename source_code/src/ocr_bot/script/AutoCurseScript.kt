package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.castSpell

/**
 *  Automatyczne pozbywanie się trucizny
 */
class AutoCurseScript: ScriptInterface {
    override fun execute(client: Client) {
        if( !client.isPoison ) {
            return
        }

        if( client.enemies.isNotEmpty() ) {
            return
        }

        client.castSpell(ScriptInterface.Spell.EXANA_POX)
    }

    override fun type() = listOf(ScriptInterface.ScriptType.HEALING)
}