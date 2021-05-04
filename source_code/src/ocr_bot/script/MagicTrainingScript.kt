package ocr_bot.script

import ocr_bot.Client
import ocr_bot.ScriptInterface
import ocr_bot.script.decoration.CooldownScript
import ocr_bot.useItem

/**
 *  Trenowanie poziomu magicznego
 */
class MagicTrainingScript(
    val potion: ScriptInterface.Item,
    val spell: ScriptInterface.Spell
): ScriptInterface {
    override fun name() = this::class.java.simpleName

    private val potionScript: ScriptInterface = CooldownScript(
        cooldownTimeInMilliseconds = 500,
        script = AutoManaPotion(potion)
    )

    private val burningScript = CooldownScript(
        cooldownTimeInMilliseconds = spell.cooldown,
        script = ManaBurningScript(
            spell = spell
        )
    )

    override fun execute(client: Client): Boolean {
        potionScript.execute(client)

        return burningScript.execute(client)
    }

    override fun type() = listOf(ScriptInterface.ScriptType.OTHER)
}