package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.ScriptType
import ocr_bot.useItem

/**
 *  Automatyczne jedzenie
 */
class AutoEat: ScriptInterface {
    override fun execute(client: Client) {
        if( !client.isHungry ) {
            return
        }

        if( !client.enemies.isNotEmpty() ) {
            return
        }

        client.useItem(ScriptInterface.Item.BROWN_MUSHROOM)
    }

    override fun type() = listOf(ScriptType.OTHER)
}