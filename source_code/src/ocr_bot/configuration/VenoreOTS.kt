package ocr_bot.configuration

import ocr_bot.ScriptInterface
import ocr_bot.script.*
import ocr_bot.script.decoration.CooldownScript
import ocr_bot.script.curse.BleedingCure
import ocr_bot.script.curse.BurningCure
import ocr_bot.script.curse.PoisonCure
import ocr_bot.script.decoration.OnceFromGroupScript

/**
 *  Konfiguracja do ots'a venore.eu
 */
class VenoreOTS: ConfigurationInterface {
    override val scripts = listOf(
//        MagicTrainingScript(
//            potion = ScriptInterface.Item.MANA_POTION,
//            spell = ScriptInterface.Spell.UTANA_VID
//        ),
        OnceFromGroupScript(
            CooldownScript(
                cooldownTimeInMilliseconds = 1000,
                script = AutoSioScript(supportCreatureWithName = "Kalan")
            ),
            PoisonCure(),
            BurningCure(),
            BleedingCure(),
            CooldownScript(
                cooldownTimeInMilliseconds = 1000,
                script = AutoHealing()
            )
        ),
        CooldownScript(
            cooldownTimeInMilliseconds = 200,
            script = AutoTargetMonsterScript()
        ),
        CooldownScript(
            cooldownTimeInMilliseconds = 200,
            script = AutoAttack(
                ignoreMonsters = listOf(
                    ScriptInterface.Monster.DOG,
                    ScriptInterface.Monster.CAT
                )
            )
        ),
//        ()
        CooldownScript(
            cooldownTimeInMilliseconds = 500,
            script = AutoManaPotion(
                potion = ScriptInterface.Item.GREAT_MANA_POTION
            )
        ),
        OnceFromGroupScript(
            CooldownScript(
                cooldownTimeInMilliseconds = 10 * 1000,
                script = AutoEat(
                    item = ScriptInterface.Item.BROWN_MUSHROOM
                )
            ),
            CooldownScript(
                cooldownTimeInMilliseconds = ScriptInterface.Spell.HASTE.cooldown,
                script = AutoHasteScript(
                    spell = ScriptInterface.Spell.HASTE
                )
            )
        )
    )

    override val delayBeetwenCaptureScreenInMilliseconds: Int = 10
}