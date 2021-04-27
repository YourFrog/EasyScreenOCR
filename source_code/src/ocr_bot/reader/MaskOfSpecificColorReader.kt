package ocr_bot.reader

import ocr_bot.MaskOfCreatureName
import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage

/**
 *  Tworzy maskę o określonym kolorze
 */
class MaskOfSpecificColorReader {

    /**
     *  Tworzy maskę z obrazka na podstawie określonego koloru
     */
    fun mask(image: BufferedImage, inputColor: Color): MaskOfCreatureName {
        val result = mutableListOf<Point>()

        for(x in 0 until image.width) {
            for(y in 0 until image.height) {
                val color = Color(image.getRGB(x, y), true)

                if( color.rgb == inputColor.rgb ) {
                    result.add(Point(x, y))
                }
            }
        }

        return result
    }
}