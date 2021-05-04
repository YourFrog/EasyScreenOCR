package ocr_bot.ui

import java.awt.Color
import java.awt.Point


open class Square (
    private val screenX: Int,
    private val screenY: Int,
    private val color: Color
) {

    fun getColor() = color

    fun getPosition() = Point(
        GAME_LEFT_TOP_X + screenX * GAME_SQUARE_WIDTH,
        GAME_LEFT_TOP_Y + screenY * GAME_SQUARE_HEIGHT
    )

    companion object {
        const val GAME_SCREEN_WIDTH = 960
        const val GAME_SCREEN_HEIGHT = 704

        const val GAME_LEFT_TOP_X = 392
        const val GAME_LEFT_TOP_Y = 78

        const val GAME_SQUARE_WIDTH = 64
        const val GAME_SQUARE_HEIGHT = 64

        const val GAME_PLAYER_POSITION_X = 7
        const val GAME_PLAYER_POSITION_Y = 5
    }
//    init {
//        preferredSize = Dimension(GAME_SQUARE_WIDTH, GAME_SQUARE_HEIGHT)
//        size = Dimension(GAME_SQUARE_WIDTH, GAME_SQUARE_HEIGHT)
//        background = Color(0, 0, 0 , 0)
//
//        setLocation(
//            screenX * GAME_SQUARE_WIDTH,
//            screenY * GAME_SQUARE_HEIGHT
//        )
//
//        isOpaque = false
////        rootPane.putClientProperty("apple.awt.draggableWindowBackground", false);
////        location = Location(20, 20)
//    }
//
//    override fun paintComponent(graphics: Graphics?) {
//        super.paintComponent(graphics)
//
//        graphics?.color = color
//
//
//        for(i in 0 .. width step 2 ) {
//            // Z lewej w dół
//            graphics?.drawLine(0, i, width - i, height)
//        }
//
//        for(i in 2 .. width step 2 ) {
//            // Z lewej w prawo
//            graphics?.drawLine(i, 0, width, height - i)
//        }
//    }
}