package ocr_bot.script.decoration

import ocr_bot.Client
import ocr_bot.ScriptInterface

/**
 *  Uruchamia jedynie jeden skrypt z grupy skryptów
 */
class OnceFromGroupScript(
    private vararg val scripts: ScriptInterface
): ScriptInterface {
    override fun name() = scripts.map { it.name() }.joinToString(", ")

    override fun execute(client: Client): Boolean {
        scripts.forEach { script ->
            script.execute(client).takeIf { it }?.let {
                return true
            }
        }

        return false
    }

    override fun type() = scripts
        .map { it.type() }
        .flatten()
        .distinct()
}