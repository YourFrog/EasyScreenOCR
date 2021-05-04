package ocr_bot.configuration

import ocr_bot.ScriptInterface

/**
 *  Kontrakt zawierany z konfiguracją
 */
interface ConfigurationInterface {
    /**
     *  Uruchamiane skrypty
     */
    val scripts: List<ScriptInterface>

    /**
     *  Minimalny czas w jakim są odczytywane ekrany
     */
    val delayBeetwenCaptureScreenInMilliseconds: Int
}