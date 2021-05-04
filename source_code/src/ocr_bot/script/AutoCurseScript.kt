package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.castSpell

/**
 *  Automatyczne pozbywanie się trucizny
 */
class AutoCurseScript: ScriptInterface {
    override fun name() = this::class.java.simpleName

    override fun execute(client: Client): Boolean {
        if( !client.isPoison ) {
            return false
        }

        if( client.enemies.isNotEmpty() ) {
            return false
        }

        return client.castSpell(ScriptInterface.Spell.EXANA_POX)
    }

    override fun type() = listOf(ScriptInterface.ScriptType.HEALING)
}