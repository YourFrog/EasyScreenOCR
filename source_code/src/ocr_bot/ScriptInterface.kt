package ocr_bot

import java.awt.event.KeyEvent

/**
 *  Kontrakt zawierany z pojedyńczym skryptem
 */
interface ScriptInterface {
    /**
     *  Nazwa skryptu
     */
    fun name(): String

    /**
     *  Wykonanie skryptu
     */
    fun execute(client: Client): Boolean

    /**
     *  Rodzaj skryptu
     */
    fun type(): List<ScriptType>

    /**
     *  Rodzaje skryptów
     */
    enum class ScriptType {
        HEALING, ATTACK, SUPPORT, OTHER
    }

    /**
     *  Informacje o zaklęciach
     */
    enum class Spell(
        val cost: Int,
        val cooldown: Int,
        val key: Int,
        val groups: List<Group>,
        val element: Element = Element.NONE,
        val averageDamage: Int = 0
    ) {
        HASTE(
            cost = 60,
            cooldown = 2000,
            key = KeyEvent.VK_J,
            groups = listOf(Group.SUPPORT)
        ),
        STRONG_HASTE(
            cost = 140,
            cooldown = 2000,
            key = KeyEvent.VK_H,
            groups = listOf(Group.SUPPORT)
        ),

        SIO(
            cost = 160,
            cooldown = 1000,
            key = KeyEvent.VK_F6,
            groups = listOf(Group.HEALING)
        ),

        UTANA_VID(
            cost = 440,
            cooldown = 1000,
            key = KeyEvent.VK_L,
            groups = listOf(Group.SUPPORT)
        ),

        EXANA_POX(
            cost = 30,
            cooldown = 6000,
            key = KeyEvent.VK_Y,
            groups = listOf(Group.HEALING)
        ),

        EXANA_FLAM(
            cost = 30,
            cooldown = 6000,
            key = KeyEvent.VK_T,
            groups = listOf(Group.HEALING)
        ),

        EXANA_KOR(
            cost = 30,
            cooldown = 6000,
            key = KeyEvent.VK_U,
            groups = listOf(Group.HEALING)
        ),

        EXURA (
            cost = 20,
            cooldown = 1000,
            key = KeyEvent.VK_O,
            groups = listOf(Group.HEALING)
        ),

        EXURA_GRAN (
            cost = 70,
            cooldown = 1000,
            key = KeyEvent.VK_P,
            groups = listOf(Group.HEALING)
        ),

        EXURA_VITA (
            cost = 160,
            cooldown = 1000,
            key = KeyEvent.VK_I,
            groups = listOf(Group.HEALING)
        ),

        UTAMO_VITA (
            cost = 50,
            cooldown = 1000,
            key = KeyEvent.VK_F8,
            groups = listOf(Group.SUPPORT)
        ),

        EXORI_MIN_FLAM (
            cost = 6,
            cooldown = 2,
            key = KeyEvent.VK_0,
            groups = listOf(Group.ATTACK),
            element = Element.FIRE,
            averageDamage = 100
        ),

        EXORI_INFIR_TERA (
            cost = 6,
            cooldown = 2,
            key = KeyEvent.VK_1,
            groups = listOf(Group.ATTACK),
            element = Element.EARTH,
            averageDamage = 100
        ),

        EXORI_MOE_ICO (
            cost = 6,
            cooldown = 20,
            key = KeyEvent.VK_2,
            groups = listOf(Group.ATTACK),
            element = Element.PHYSICAL,
            averageDamage = 220
        ),

        EXORI_TERA (
            cost = 20,
            cooldown = 2,
            key = KeyEvent.VK_4,
            groups = listOf(Group.ATTACK),
            element = Element.EARTH,
            averageDamage = 220
        ),

        EXORI_FRIGO (
            cost = 20,
            cooldown = 2,
            key = KeyEvent.VK_5,
            groups = listOf(Group.ATTACK),
            element = Element.ICE,
            averageDamage = 220
        ),

        EXORI_FLAM (
            cost = 20,
            cooldown = 2,
            key = KeyEvent.VK_6,
            groups = listOf(Group.ATTACK),
            element = Element.FIRE,
            averageDamage = 220
        ),

        EXORI_VIS (
            cost = 20,
            cooldown = 2,
            key = KeyEvent.VK_7,
            groups = listOf(Group.ATTACK),
            element = Element.ENERGY,
            averageDamage = 220
        );

        enum class Group {
            ATTACK,
            HEALING,
            SUPPORT,
            SPECIAL,
            CONJURE
        }
    }

    /**
     *  Informacje o przedmiotach
     */
    enum class Item(val key: Int) {
        STRONG_MANA_POTION(KeyEvent.VK_F2),
        MANA_POTION(KeyEvent.VK_F2),
        GREAT_MANA_POTION(KeyEvent.VK_F2),
        ULTIMATE_MANA_POTION(KeyEvent.VK_F2),

        BROWN_MUSHROOM(KeyEvent.VK_F)
    }

    enum class Rune(val key: Int, val cost: Int, val element: Element, val averageDamage: Int, val singleTarget: Boolean) {
        RUNE_SD(
            key = KeyEvent.VK_M,
            cost = 135,
            element = Element.DEATH,
            averageDamage = 500,
            singleTarget = true
        ),
        RUNE_ICYCLE(
            KeyEvent.VK_F4,
            cost = 30,
            element = Element.ICE,
            averageDamage = 200,
            singleTarget = true
        ),
        RUNE_AVALANCHE(
            KeyEvent.VK_F5,
            cost = 57,
            element = Element.ICE,
            averageDamage = 200,
            singleTarget = false
        )
    }

    /**
     *  Potwory
     */
    enum class Monster(val monsterName: String, val maxHealth: Int, val resistances: List<Resistance>) {
        FRAZZLEMAW(monsterName = "Frazzlemaw", maxHealth = 4100, resistances = listOf(
            Resistance(Element.EARTH, 20),
            Resistance(Element.ENERGY, 15),
            Resistance(Element.FIRE, 10),
            Resistance(Element.DEATH, 10),
            Resistance(Element.ICE, 5),
            Resistance(Element.PHYSICAL, 5),
            Resistance(Element.HOLY, -5)
        )),
        SILENCER(monsterName = "Silencer", maxHealth = 5400, resistances = listOf(
            Resistance(Element.EARTH, 100),
            Resistance(Element.DEATH, 65),
            Resistance(Element.FIRE, 30),
            Resistance(Element.ENERGY, 15),
            Resistance(Element.ICE, 15),
            Resistance(Element.PHYSICAL, 5),
            Resistance(Element.HOLY, -25)
        )),

        DEMON(monsterName = "Demon", maxHealth = 8200, resistances = listOf(
            Resistance(Element.FIRE, 100),
            Resistance(Element.ENERGY, 50),
            Resistance(Element.EARTH, 40),
            Resistance(Element.PHYSICAL, 25),
            Resistance(Element.DEATH, 20),
            Resistance(Element.ICE, -12),
            Resistance(Element.HOLY, -12)
        )),
        VEXCLAW(monsterName = "Vexclaw", maxHealth = 8500, resistances = listOf(
            Resistance(Element.FIRE, 75),
            Resistance(Element.EARTH, 40),
            Resistance(Element.DEATH, 20),
            Resistance(Element.ENERGY, 10),
            Resistance(Element.PHYSICAL, 5),
            Resistance(Element.ICE, -5),
            Resistance(Element.HOLY, -10)
        )),
        GRIMELEECH(monsterName = "Grimeleech", maxHealth = 9500, resistances = listOf(
            Resistance(Element.DEATH, 60),
            Resistance(Element.EARTH, 40),
            Resistance(Element.FIRE, 20),
            Resistance(Element.ICE, 0),
            Resistance(Element.HOLY, 0),
            Resistance(Element.PHYSICAL, 0),
            Resistance(Element.ENERGY, -5)
        )),

        DRAGON(monsterName = "Dragon", maxHealth = 1000, resistances = listOf(
            Resistance(Element.DEATH, 0),
            Resistance(Element.EARTH, 80),
            Resistance(Element.FIRE, 100),
            Resistance(Element.ICE, -10),
            Resistance(Element.HOLY, 0),
            Resistance(Element.PHYSICAL, 0),
            Resistance(Element.ENERGY, 20)
        )),

        DRAGON_LORD(monsterName = "Dragon Lord", maxHealth = 1900, resistances = listOf(
            Resistance(Element.DEATH, 0),
            Resistance(Element.EARTH, 80),
            Resistance(Element.FIRE, 100),
            Resistance(Element.ICE, -10),
            Resistance(Element.HOLY, 0),
            Resistance(Element.PHYSICAL, 0),
            Resistance(Element.ENERGY, 20)
        )),

        RAT(monsterName = "Rat", maxHealth = 20, resistances = listOf(
            Resistance(Element.DEATH, -10),
            Resistance(Element.EARTH, 20),
            Resistance(Element.FIRE, 0),
            Resistance(Element.ICE, -10),
            Resistance(Element.HOLY, 20),
            Resistance(Element.PHYSICAL, 0),
            Resistance(Element.ENERGY, 0)
        )),

        CAVE_RAT(monsterName = "Cave Rat", maxHealth = 30, resistances = listOf(
            Resistance(Element.DEATH, 0),
            Resistance(Element.EARTH, 0),
            Resistance(Element.FIRE, -10),
            Resistance(Element.ICE, 0),
            Resistance(Element.HOLY, 0),
            Resistance(Element.PHYSICAL, 0),
            Resistance(Element.ENERGY, 0)
        )),

        WILD_WARRIOR(monsterName = "Wild Warrior", maxHealth = 20, resistances = listOf(
            Resistance(Element.DEATH, -5),
            Resistance(Element.EARTH, 0),
            Resistance(Element.FIRE, 0),
            Resistance(Element.ICE, 0),
            Resistance(Element.HOLY, 10),
            Resistance(Element.PHYSICAL, -5),
            Resistance(Element.ENERGY, 0)
        )),

        VALKYRIE(monsterName = "Valkyrie", maxHealth = 20, resistances = listOf(
            Resistance(Element.DEATH, -5),
            Resistance(Element.EARTH, 0),
            Resistance(Element.FIRE, 10),
            Resistance(Element.ICE, 10),
            Resistance(Element.HOLY, 5),
            Resistance(Element.PHYSICAL, -10),
            Resistance(Element.ENERGY, 0)
        )),

        AMAZON(monsterName = "Amazon", maxHealth = 20, resistances = listOf(
            Resistance(Element.DEATH, -5),
            Resistance(Element.EARTH, 0),
            Resistance(Element.FIRE, 0),
            Resistance(Element.ICE, 0),
            Resistance(Element.HOLY, 0),
            Resistance(Element.PHYSICAL, -10),
            Resistance(Element.ENERGY, 0)
        )),

        DOG(monsterName = "Dog", maxHealth = 20, resistances = listOf(
            Resistance(Element.DEATH, 0),
            Resistance(Element.EARTH, 0),
            Resistance(Element.FIRE, 0),
            Resistance(Element.ICE, 0),
            Resistance(Element.HOLY, 0),
            Resistance(Element.PHYSICAL, 0),
            Resistance(Element.ENERGY, 0)
        )),

        CAT(monsterName = "Cat", maxHealth = 20, resistances = listOf(
            Resistance(Element.DEATH, 0),
            Resistance(Element.EARTH, 0),
            Resistance(Element.FIRE, 0),
            Resistance(Element.ICE, 0),
            Resistance(Element.HOLY, 0),
            Resistance(Element.PHYSICAL, 0),
            Resistance(Element.ENERGY, 0)
        ));

        companion object {
            @JvmStatic
            fun from(creatureName: String): Monster? {
                return values().firstOrNull()    {
                    it.monsterName == creatureName
                }
            }
        }
    }

    /**
     *  Klasa opisująca odporność
     */
    class Resistance(
        /**
         *  Żywioł
         */
        val element: Element,

        /**
         *  Wartość procentowa
         */
        val percent: Int
    )

    /**
     *  Żywioły
     */
    enum class Element {
        NONE, EARTH, ENERGY, FIRE, DEATH, ICE, PHYSICAL, HOLY
    }
}