package ocr_bot.script.curse

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.ScriptInterface.Spell
import ocr_bot.castFirstSpell
import ocr_bot.hasEnemies

/**
 *  Abstrakcja która upraszcza skrypty do "leczenia ze statusów"
 */
abstract class AbstractCure: ScriptInterface {
    /**
     *  Zaklęcie do leczenia
     */
    abstract fun spell(): Spell

    /**
     *  Sprawdzenie stanu gracza
     */
    abstract fun isNeedCure(client: Client): Boolean

    override fun name() = this::class.java.toString()

    override fun execute(client: Client): Boolean {
        if( client.isHealingCooldown ) {
            return false
        }

        if( client.hasEnemies() ) {
            return false
        }

        if( isNeedCure(client) ) {
            return client.castFirstSpell(spell(), Spell.EXURA)
        }

        return false
    }

    override fun type() = listOf(ScriptInterface.ScriptType.HEALING)
}