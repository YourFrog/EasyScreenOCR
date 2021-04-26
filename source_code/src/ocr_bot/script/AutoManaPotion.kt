package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.Item
import ocr_bot.ScriptInterface.ScriptType
import ocr_bot.useItem

/**
 *  Automatyczne używanie potiona na odnowienie many
 */
class AutoManaPotion(
    private val potion: Item
): ScriptInterface {
    override fun execute(client: Client) {
        val needPotion = when {
            client.enemies.isEmpty() && client.mana.percent() < 90 -> true
            client.enemies.size == 1 && client.isAttackCooldown && client.mana.percent() < 50 -> true
            client.enemies.size in 2 .. 3 && client.isAttackCooldown && client.mana.percent() < 65 -> true
            client.enemies.size > 3 && client.isAttackCooldown && client.mana.percent() < 80 -> true
            else -> false
        }

        if( needPotion ) {
            client.useItem(potion)
        }
    }

    override fun type() = listOf(ScriptType.OTHER)
}