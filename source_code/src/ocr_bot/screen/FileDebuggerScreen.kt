package ocr_bot.screen

import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 *  Odczytywanie ekranu z pliku
 */
class FileDebuggerScreen(
    private val filename: String
): ScreenInterface {
    private lateinit var cache: BufferedImage

    init {
        refresh()
    }

    override fun capture(sector: Rectangle): BufferedImage {
        return cache.getSubimage(sector.x, sector.y, sector.width, sector.height)
    }

    override fun capture(): BufferedImage {
        return cache
    }

    override fun refresh() {
        val file = File(filename)

        cache = ImageIO.read(file)
    }
}