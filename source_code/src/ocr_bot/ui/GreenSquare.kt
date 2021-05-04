package ocr_bot.ui

import java.awt.Color

class GreenSquare (
    private val screenX: Int,
    private val screenY: Int
): Square(screenX, screenY, Color(0, 255, 0, 120))