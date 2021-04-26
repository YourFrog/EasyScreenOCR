package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.ScriptType
import ocr_bot.noHasTarget
import java.awt.event.KeyEvent

/**
 *  Automatyczne zaznaczanie potwora
 */
class AutoTargetMonsterScript: ScriptInterface {
    override fun execute(client: Client) {
        if( client.enemies.isEmpty() ) {
            return
        }

        if( client.noHasTarget() ) {
            client.keyboard.keyPress(KeyEvent.VK_SPACE)
            client.keyboard.keyRelease(KeyEvent.VK_SPACE)
        }
    }

    override fun type() = listOf(ScriptType.OTHER)
}