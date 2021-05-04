package ocr_bot.script.curse

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.script.decoration.CooldownScript

/**
 *  Leczenie z podpalenia
 */
class BurningCure: CooldownScript(
    cooldownTimeInMilliseconds = ScriptInterface.Spell.EXANA_FLAM.cooldown,
    script = object : AbstractCure() {
        override fun spell() = ScriptInterface.Spell.EXANA_FLAM
        override fun isNeedCure(client: Client) = client.isBurning
    }
) {
    override fun name() = this::class.java.toString()
}