package ocr_bot.ui

import java.awt.Color

class BlueSquare (
    private val screenX: Int,
    private val screenY: Int
): Square(screenX, screenY, Color(0, 0, 255, 120))