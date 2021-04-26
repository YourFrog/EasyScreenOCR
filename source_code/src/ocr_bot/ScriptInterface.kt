package ocr_bot

import java.awt.event.KeyEvent

/**
 *  Kontrakt zawierany z pojedyńczym skryptem
 */
interface ScriptInterface {
    /**
     *  Wykonanie skryptu
     */
    fun execute(client: Client)

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
    enum class Spell(val cost: Int, val key: Int, val groups: List<Group>) {
        HASTE(
            cost = 140,
            key = KeyEvent.VK_H,
            groups = listOf(Group.SUPPORT)
        ),

        SIO(
            cost = 160,
            key = KeyEvent.VK_F6,
            groups = listOf(Group.HEALING)
        ),

        UTANA_VID(
            cost = 440,
            key = KeyEvent.VK_L,
            groups = listOf(Group.SUPPORT)
        ),

        EXANA_POX(
            cost = 30,
            key = KeyEvent.VK_U,
            groups = listOf(Group.HEALING)
        ),

        EXURA (
            cost = 20,
            key = KeyEvent.VK_O,
            groups = listOf(Group.HEALING)
        ),

        EXURA_GRAN (
            cost = 70,
            key = KeyEvent.VK_P,
            groups = listOf(Group.HEALING)
        ),

        EXURA_VITA (
            cost = 160,
            key = KeyEvent.VK_I,
            groups = listOf(Group.HEALING)
        ),

        UTAMO_VITA (
            cost = 50,
            key = KeyEvent.VK_F8,
            groups = listOf(Group.SUPPORT)
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

        RUNE_SD(KeyEvent.VK_M),
        RUNE_ICYCLE(KeyEvent.VK_F4),
        RUNE_AVALANCHE(KeyEvent.VK_F5),

        BROWN_MUSHROOM(KeyEvent.VK_F)
    }

    /**
     *  Potwory
     */
    enum class Monster(val monsterName: String) {
        FRAZZLEMAW(monsterName = "Frazzlemaw"),
        SILENCER(monsterName = "Silencer"),
        LIZARD_CHOSEN(monsterName = "L izar d Chosen"), //"Lizard Chosen"),

        DEMON(monsterName = "Demon"),
        VEXCLAW(monsterName = "Vexclaw"),
        GRIMELEECH(monsterName = "Grimeleech"),

        DOG(monsterName = "Dog");

        companion object {
            @JvmStatic
            fun from(creatureName: String): Monster? {
                return values().firstOrNull() {
                    it.monsterName == creatureName
                }
            }
        }
    }
}