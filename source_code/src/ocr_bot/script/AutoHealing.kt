package ocr_bot.script

import ocr_bot.*
import ocr_bot.ScriptInterface.Spell

/**
 *  Skrypt próbuje utrzymać przy życiu postać używając odpowiedniego zaklęcia, a jeżeli się to nie powiedzie to odpalając utame
 */
class AutoHealing: ScriptInterface {
    override fun execute(client: Client) {
        if( client.isHealingCooldown ) {
            noManaOrHealingIsDisable(client)
            return
        }

        val spellIsCast = when {
            client.health.percent() in 90 .. 99 -> client.castSpell(Spell.EXURA)
            client.health.percent() in 75 .. 90 -> client.castFirstSpell(Spell.EXURA_GRAN, Spell.EXURA)
            client.health.percent() in -1 .. 75 ->  client.castFirstSpell(Spell.EXURA_VITA, Spell.EXURA_GRAN, Spell.EXURA)
            else -> false
        }

        if( !spellIsCast ) {
            noManaOrHealingIsDisable(client)
        }
    }

    private fun noManaOrHealingIsDisable(client: Client) {
        if( client.health.percent() < 30 ) {
            client.castSpell(Spell.UTAMO_VITA)
        }
    }

    override fun type() = listOf(ScriptInterface.ScriptType.HEALING)
}