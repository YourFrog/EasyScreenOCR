package ocr_bot.ui.controls

import ocr_bot.ScriptInterface
import ocr_bot.ui.Square
import java.awt.Color
import java.awt.Dimension
import javax.swing.JCheckBox

/**
 *  Checkbox wspierający uruchomienie skryptów
 */
class ScriptCheckbox(
    val script: ScriptInterface
): JCheckBox() {

    init {
        text = script.name()

        background = Color(0, 0, 0, 0)
        foreground = Color.ORANGE

        preferredSize = Dimension(200, 20)
        size = Dimension(200, 20)
    }
}