package ocr_bot.script.decoration

import ocr_bot.Client
import ocr_bot.ScriptInterface

/**
 *  Nakładka dodająca cooldown na skrypty
 */
open class CooldownScript(
    private val cooldownTimeInMilliseconds: Int,
    private val script: ScriptInterface
) : ScriptInterface {
    var lastSuccessRunningInMilliseconds = 0L


    override fun name() = script.name()

    override fun execute(client: Client): Boolean {
        val currentTimeInMilliseconds = System.currentTimeMillis()

        if( lastSuccessRunningInMilliseconds + cooldownTimeInMilliseconds > currentTimeInMilliseconds ) {
            return false
        }

        val result = script.execute(client)

        if( result ) {
            lastSuccessRunningInMilliseconds = currentTimeInMilliseconds
        }

        return result
    }

    override fun type() = script.type()
}