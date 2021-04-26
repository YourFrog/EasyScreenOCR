package ocr_bot.screen

import java.awt.Rectangle
import java.awt.image.BufferedImage

/**
 * Interfejs obsługujący ekran
 */
interface ScreenInterface {
    /**
     *  Odczytanie części ekranu
     */
    fun capture(sector: Rectangle): BufferedImage

    /**
     *  Odczytanie całości ekranu
     */
    fun capture(): BufferedImage

    /**
     *  Odświeżenie screena
     */
    fun refresh()
}