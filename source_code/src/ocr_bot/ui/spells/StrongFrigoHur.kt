package ocr_bot.ui.spells

import ocr_bot.ui.BlueSquare
import ocr_bot.ui.Square


class StrongFrigoHur: SpellHighlight() {
    override fun squaresInRightSide() = listOf(
        BlueSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y + 0),
        BlueSquare(Square.GAME_PLAYER_POSITION_X + 2, Square.GAME_PLAYER_POSITION_Y + 0),
        BlueSquare(Square.GAME_PLAYER_POSITION_X + 3, Square.GAME_PLAYER_POSITION_Y + 0),
        BlueSquare(Square.GAME_PLAYER_POSITION_X + 2, Square.GAME_PLAYER_POSITION_Y + 1),
        BlueSquare(Square.GAME_PLAYER_POSITION_X + 3, Square.GAME_PLAYER_POSITION_Y + 1),

        BlueSquare(Square.GAME_PLAYER_POSITION_X + 2, Square.GAME_PLAYER_POSITION_Y + -1),
        BlueSquare(Square.GAME_PLAYER_POSITION_X + 3, Square.GAME_PLAYER_POSITION_Y + -1)
    )

    override fun squaresInLeftSide() = listOf(
        BlueSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y + 0),
        BlueSquare(Square.GAME_PLAYER_POSITION_X - 2, Square.GAME_PLAYER_POSITION_Y + 0),
        BlueSquare(Square.GAME_PLAYER_POSITION_X - 3, Square.GAME_PLAYER_POSITION_Y + 0),
        BlueSquare(Square.GAME_PLAYER_POSITION_X - 2, Square.GAME_PLAYER_POSITION_Y + 1),
        BlueSquare(Square.GAME_PLAYER_POSITION_X - 3, Square.GAME_PLAYER_POSITION_Y + 1),

        BlueSquare(Square.GAME_PLAYER_POSITION_X - 2, Square.GAME_PLAYER_POSITION_Y + -1),
        BlueSquare(Square.GAME_PLAYER_POSITION_X - 3, Square.GAME_PLAYER_POSITION_Y + -1)
    )

    override fun squaresInUpSide() = listOf(
        BlueSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y + 1),
        BlueSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y + 2),
        BlueSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y + 3),

        BlueSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y + 2),
        BlueSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y + 3),

        BlueSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y + 2),
        BlueSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y + 3)
    )

    override fun squaresInBottomSide() = listOf(
        BlueSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y - 1),
        BlueSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y - 2),
        BlueSquare(Square.GAME_PLAYER_POSITION_X, Square.GAME_PLAYER_POSITION_Y - 3),

        BlueSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y - 2),
        BlueSquare(Square.GAME_PLAYER_POSITION_X + 1, Square.GAME_PLAYER_POSITION_Y - 3),

        BlueSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y - 2),
        BlueSquare(Square.GAME_PLAYER_POSITION_X - 1, Square.GAME_PLAYER_POSITION_Y - 3)
    )
}