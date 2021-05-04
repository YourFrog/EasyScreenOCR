package ocr_bot.script

import ocr_bot.*
import ocr_bot.ScriptInterface.Spell

/**
 *  Skrypt próbuje utrzymać przy życiu postać używając odpowiedniego zaklęcia, a jeżeli się to nie powiedzie to odpalając utame
 */
class AutoHealing: ScriptInterface {
    override fun name() = this::class.java.simpleName

    override fun execute(client: Client): Boolean {
        if( client.isHealingCooldown ) {
            noManaOrHealingIsDisable(client)
            return false
        }

        val healthPercent = client.health.percent()

        val spellIsCast = when {
            healthPercent in 90 .. 99 -> client.castSpell(Spell.EXURA)
            healthPercent in 75 .. 90 -> client.castFirstSpell(Spell.EXURA_GRAN, Spell.EXURA)
            healthPercent in -1 .. 75 ->  client.castFirstSpell(Spell.EXURA_VITA, Spell.EXURA_GRAN, Spell.EXURA)
            else -> false
        }

        if( !spellIsCast ) {
            return noManaOrHealingIsDisable(client)
        }

        return true
    }

    private fun noManaOrHealingIsDisable(client: Client): Boolean {
        if( client.health.percent() < 30 ) {
            return client.castSpell(Spell.UTAMO_VITA)
        }

        return false
    }

    override fun type() = listOf(ScriptInterface.ScriptType.HEALING)
}