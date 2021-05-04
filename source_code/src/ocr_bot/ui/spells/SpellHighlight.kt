package ocr_bot.ui.spells

import ocr_bot.ui.Square
import java.awt.Color
import java.awt.Dimension
import javax.swing.JPanel

abstract class SpellHighlight {
    abstract fun squaresInRightSide(): List<Square>
    abstract fun squaresInLeftSide(): List<Square>
    abstract fun squaresInUpSide(): List<Square>
    abstract fun squaresInBottomSide(): List<Square>
}