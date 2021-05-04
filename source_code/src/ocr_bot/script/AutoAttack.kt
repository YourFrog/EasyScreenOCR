package ocr_bot.script

import ocr_bot.*
import ocr_bot.ScriptInterface.ScriptType
import kotlin.math.ceil
import kotlin.math.floor

typealias RuneWithDamage = Pair<ScriptInterface.Rune, Int>
typealias SpellWithDamage = Pair<ScriptInterface.Spell, Int>

/**
 *  Skrypt automatycznie atakujący potwory
 */
class AutoAttack(
    val ignoreMonsters: List<ScriptInterface.Monster> = emptyList(),
    val availableSpells: List<ScriptInterface.Spell> = listOf(
        ScriptInterface.Spell.EXORI_MIN_FLAM,
        ScriptInterface.Spell.EXORI_INFIR_TERA,

        ScriptInterface.Spell.EXORI_MOE_ICO,
        ScriptInterface.Spell.EXORI_TERA,
        ScriptInterface.Spell.EXORI_FRIGO,
        ScriptInterface.Spell.EXORI_FLAM,
        ScriptInterface.Spell.EXORI_VIS
    ),
    val availableRunes: List<ScriptInterface.Rune> = listOf(
        ScriptInterface.Rune.RUNE_SD
//        ScriptInterface.Item.RUNE_AVALANCHE,
//        ScriptInterface.Item.RUNE_ICYCLE
    )
): ScriptInterface {
    override fun name() = this::class.java.simpleName

    override fun execute(client: Client): Boolean {
        if( client.isAttackCooldown ) {
            return false
        }

        if( client.noHasTarget() ) {
            return false
        }

        return client.getTargetCreature()?.let { creature ->
            when(client.enemies.size) {
                1 -> attackSingleCreature(client, creature)
                else -> attackMultipleCreature(client, creature)
            }
        } ?: false
    }

    override fun type() = listOf(ScriptType.ATTACK)

    /**
     *  Zaatakowanie pojedyńczego potwora przy użyciu najefektywniejszego zaklęcia
     */
    private fun attackSingleCreature(client: Client, creature: Creature): Boolean {
        val monster = ScriptInterface.Monster.from(creature.name)

        if( monster == null ) {
            return client.useRune(ScriptInterface.Rune.RUNE_SD)
        }

        if( ignoreMonsters.contains(monster) ) {
            return false
        }

        val currentHealth = ceil(monster.maxHealth * creature.healthPercent / 100.0).toInt()

        val theBestSpell = selectTheBestSpell(client.mana.current, currentHealth, monster)
        val theBestRune = selectTheBestRune(currentHealth, monster)

        if( theBestRune == null && theBestSpell == null ) {
            return false
        }

        if( theBestSpell == null ) {
            return client.useRune(theBestRune!!.first)
        }

        if( theBestRune == null ) {
            return client.castSpell(theBestSpell.first)
        }


        if( theBestRune.second > currentHealth && theBestSpell.second > currentHealth ) {
            // Oba zabijają bierz tańszy
            val costOfSpellInGP = theBestSpell.first.cost * 0.6 // 1 many kosztuje około 0.6 GP

            if( theBestRune.first.cost > costOfSpellInGP ) {
                return client.castSpell(theBestSpell.first)
            } else {
                return client.useRune(theBestRune.first)
            }
        }

        if( theBestRune.second >= currentHealth || theBestSpell.second >= currentHealth ) {
            // Jeden z nich zabija weź ten co zabija
            if( theBestRune.second >= currentHealth ) {
                return client.useRune(theBestRune.first)
            } else {
                return client.castSpell(theBestSpell.first)
            }
        }

        // Weź tego co zada większe obrażenia
        return if( theBestRune.second > theBestSpell.second ) {
            client.useRune(theBestRune.first)
        } else {
            client.castSpell(theBestSpell.first)
        }

//
//        val item = when(monster) {
//            ScriptInterface.Monster.FRAZZLEMAW -> ScriptInterface.Item.RUNE_SD
//            ScriptInterface.Monster.SILENCER -> ScriptInterface.Item.RUNE_ICYCLE
//            ScriptInterface.Monster.GRIMELEECH -> ScriptInterface.Item.RUNE_ICYCLE
////            ScriptInterface.Monster.LIZARD_CHOSEN -> ScriptInterface.Item.RUNE_SD
//            ScriptInterface.Monster.DEMON -> ScriptInterface.Item.RUNE_SD
//            ScriptInterface.Monster.VEXCLAW -> ScriptInterface.Item.RUNE_SD
//            ScriptInterface.Monster.DRAGON -> ScriptInterface.Item.RUNE_SD
//            ScriptInterface.Monster.DRAGON_LORD -> ScriptInterface.Item.RUNE_SD
//            else -> {
//                System.out.println("Unknown creature: " + creature.name)
//                ScriptInterface.Item.RUNE_SD
//            }
//        }
//
//        client.useItem(item)
//        return true
    }

    /**
     *  Próba zaatakowania wielu potworów
     */
    private fun attackMultipleCreature(client: Client, creature: Creature): Boolean {
//        val item = ScriptInterface.Rune.RUNE_AVALANCHE
        val item = ScriptInterface.Rune.RUNE_SD

        client.useRune(item)
        return true
    }

    /**
     *  Wybiera najlepszą rune z pośród dozwolonych, najpierw bierze najtańszą która zabije,
     *  a później to które zada najwięcej obrażeń
     */
    private fun selectTheBestRune(creatureHealth: Int, monster: ScriptInterface.Monster): RuneWithDamage? {
        val runeWithDamage = availableRunes.map { rune ->
            val resistanceInPercent = getResistanceInPercent(monster, rune.element)

            val damage = rune.averageDamage
            val realDamage = floor(damage - damage * (resistanceInPercent / 100.0)).toInt()

            RuneWithDamage(rune, realDamage)
        }

        val cheapestKillingRune = runeWithDamage
            .asSequence()
            .filter { it.second > creatureHealth }
            .sortedBy { it.first.cost }
            .firstOrNull()

        if( cheapestKillingRune != null ) {
            return cheapestKillingRune
        }

        val mostDamageRune = runeWithDamage
            .sortedByDescending { it.first.averageDamage }
            .firstOrNull()

        if( mostDamageRune != null ) {
            return mostDamageRune
        }

        return null
    }

    /**
     *  Wybiera najlepsze zaklęcie z pośród dozwolonych, najpierw bierze najtańsze które zabije,
     *  a później to które zada najwięcej obrażeń
     */
    private fun selectTheBestSpell(currentMana: Int, creatureHealth: Int, monster: ScriptInterface.Monster): SpellWithDamage? {
        val spellsWithDamage = availableSpells.map { spell ->
            val resistanceInPercent = getResistanceInPercent(monster, spell.element)

            val damage = spell.averageDamage
            val realDamage = floor(damage - damage * (resistanceInPercent / 100.0)).toInt()

            SpellWithDamage(spell, realDamage)
        }

        val cheapestKillingSpell = spellsWithDamage
            .asSequence()
            .filter { it.first.cost <= currentMana }
            .filter { it.second > creatureHealth }
            .sortedBy { it.first.cost }
            .firstOrNull()

        if( cheapestKillingSpell != null ) {
            return cheapestKillingSpell
        }

        val mostDamageSpell = spellsWithDamage
            .filter { it.first.cost <= currentMana }
            .sortedByDescending { it.first.averageDamage }
            .firstOrNull()

        if( mostDamageSpell != null ) {
            return mostDamageSpell
        }

        return null
    }

    /**
     *  Odporność procentowa potwora na żywioł, w przypadku braku zakładam brak czynników potwora
     */
    private fun getResistanceInPercent(monster: ScriptInterface.Monster, element: ScriptInterface.Element): Int {
        return monster.resistances.firstOrNull { it.element == element }?.percent ?: 0
    }
}