package ocr_bot.ui.spells

import ocr_bot.ui.GreenSquare
import ocr_bot.ui.Square


class TerraWave: SpellHighlight() {
    override fun squaresInRightSide() = listOf(
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y + 0),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 2, Square.GAME_PLAYER_POSITION_Y + 0),

        GreenSquare(Square.GAME_PLAYER_POSITION_X + 3, Square.GAME_PLAYER_POSITION_Y + 0),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 4, Square.GAME_PLAYER_POSITION_Y + 0),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 5, Square.GAME_PLAYER_POSITION_Y + 0),

        GreenSquare(Square.GAME_PLAYER_POSITION_X + 3, Square.GAME_PLAYER_POSITION_Y + 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 4, Square.GAME_PLAYER_POSITION_Y + 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 5, Square.GAME_PLAYER_POSITION_Y + 1),

        GreenSquare(Square.GAME_PLAYER_POSITION_X + 3, Square.GAME_PLAYER_POSITION_Y - 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 4, Square.GAME_PLAYER_POSITION_Y - 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 5, Square.GAME_PLAYER_POSITION_Y - 1)
    )

    override fun squaresInLeftSide() = listOf(
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y + 0),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 2, Square.GAME_PLAYER_POSITION_Y + 0),

        GreenSquare(Square.GAME_PLAYER_POSITION_X - 3, Square.GAME_PLAYER_POSITION_Y + 0),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 4, Square.GAME_PLAYER_POSITION_Y + 0),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 5, Square.GAME_PLAYER_POSITION_Y + 0),

        GreenSquare(Square.GAME_PLAYER_POSITION_X - 3, Square.GAME_PLAYER_POSITION_Y + 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 4, Square.GAME_PLAYER_POSITION_Y + 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 5, Square.GAME_PLAYER_POSITION_Y + 1),

        GreenSquare(Square.GAME_PLAYER_POSITION_X - 3, Square.GAME_PLAYER_POSITION_Y - 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 4, Square.GAME_PLAYER_POSITION_Y - 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 5, Square.GAME_PLAYER_POSITION_Y - 1)
    )

    override fun squaresInUpSide() = listOf(
        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y - 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y - 2),

        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y - 3),
        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y - 4),
        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y - 5),

        GreenSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y - 3),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y - 4),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y - 5),

        GreenSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y - 3),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y - 4),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y - 5)
    )

    override fun squaresInBottomSide() = listOf(
        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y + 1),
        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y + 2),

        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y + 3),
        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y + 4),
        GreenSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y + 5),

        GreenSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y + 3),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y + 4),
        GreenSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y + 5),

        GreenSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y + 3),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y + 4),
        GreenSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y + 5)
    )
}