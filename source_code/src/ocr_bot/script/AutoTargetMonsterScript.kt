package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.ScriptType
import ocr_bot.noHasTarget
import ocr_bot.pressAndRelease
import java.awt.event.KeyEvent

/**
 *  Automatyczne zaznaczanie potwora
 */
class AutoTargetMonsterScript: ScriptInterface {
    override fun name() = this::class.java.simpleName

    override fun execute(client: Client): Boolean {
        if( client.enemies.isEmpty() ) {
            return false
        }

        if( client.noHasTarget() ) {
            client.pressAndRelease(KeyEvent.VK_SPACE)
            return true
        }

        return false
    }

    override fun type() = listOf(ScriptType.OTHER)
}