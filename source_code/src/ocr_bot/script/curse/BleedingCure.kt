package ocr_bot.script.curse

import ocr_bot.Client
import ocr_bot.ScriptInterface.Spell
import ocr_bot.script.decoration.CooldownScript

/**
 *  Leczenie z krwawienia
 */
class BleedingCure: CooldownScript(
    cooldownTimeInMilliseconds = Spell.EXANA_KOR.cooldown,
    script = object : AbstractCure() {
        override fun spell() = Spell.EXANA_KOR
        override fun isNeedCure(client: Client) = client.isBleeding
    }
) {
    override fun name() = this::class.java.toString()
}