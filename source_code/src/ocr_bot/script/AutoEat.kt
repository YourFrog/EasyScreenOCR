package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.ScriptType
import ocr_bot.useItem

/**
 *  Automatyczne jedzenie
 */
class AutoEat(
    val item: ScriptInterface.Item
): ScriptInterface {
    override fun name() = this::class.java.simpleName

    override fun execute(client: Client): Boolean {
        if( !client.isHungry ) {
            return false
        }

        if( client.enemies.isNotEmpty() ) {
            return false
        }

        return client.useItem(item)
    }

    override fun type() = listOf(ScriptType.OTHER)
}