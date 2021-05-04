package ocr_bot.script.curse

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.script.decoration.CooldownScript

/**
 *  Leczenie z otrucia
 */
class PoisonCure: CooldownScript(
    cooldownTimeInMilliseconds = ScriptInterface.Spell.EXANA_POX.cooldown,
    script = object : AbstractCure() {
        override fun spell() = ScriptInterface.Spell.EXANA_POX
        override fun isNeedCure(client: Client) = client.isPoison
    }
) {
    override fun name() = this::class.java.toString()
}