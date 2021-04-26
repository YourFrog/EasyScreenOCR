package ocr_bot.screen

import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage

/**
 *  Obsługa prawdziwego ekranu
 */
class RealtimeScreen: ScreenInterface {
    /**
     *  Robot wykorzystywany do przechwytywania ekranu
     */
    private val robot = Robot()

    /**
     *  Ekran który został przechwycony
     */
    private lateinit var cache: BufferedImage

    override fun capture(sector: Rectangle): BufferedImage {
        return cache.getSubimage(sector.x, sector.y, sector.width, sector.height)
    }

    override fun capture(): BufferedImage {
        return cache
    }

    override fun refresh() {
        val screen = Toolkit.getDefaultToolkit().screenSize
        val area = Rectangle(screen)

        cache = robot.createScreenCapture(area)
    }
}