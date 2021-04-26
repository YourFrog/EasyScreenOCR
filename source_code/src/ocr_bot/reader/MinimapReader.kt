package ocr_bot.reader

import ocr_bot.screen.ScreenInterface
import java.awt.Point
import java.awt.Rectangle
import java.io.File
import javax.imageio.ImageIO

/**
 *  Klasa wykorzystywana do odczytywania minimapki
 */
class MinimapReader {

    /**
     *  Odczytanie współrzędnych widocznych na ekranie
     */
    fun read(recorder: ScreenInterface, zoom: Zoom = Zoom.MAXIMUM): List<Point> {
        val sector = Rectangle(1753, 25, 106, 108)
        val imageOfMinimap = recorder.capture(sector)

        ImageIO.write(imageOfMinimap, "png", File("C:\\Users\\YourFrog\\Documents\\Tibia\\debug-minimap.png"))

        /* Przesunięcie do następnego pixela */
        val skip = when(zoom) {
            Zoom.MAXIMUM -> 4
            Zoom.MINIMUM -> 2
            Zoom.MEDIUM -> 1
        }

        // cross 6x6
        // margin_left: 51
        // margin_right: 53
        // margin_top: 51
        // margin_bottom: 52
        return emptyList()
    }

    enum class Zoom {
        MAXIMUM, MEDIUM, MINIMUM
    }
}