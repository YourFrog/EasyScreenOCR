package ocr_bot

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 *  Manager obsługujący pliki graficzne związane z OCR
 */
class LetterManager {
    private val numbers = arrayOf(
        Pair("0", "0"),
        Pair("1", "1"),
        Pair("2", "2"),
        Pair("3", "3"),
        Pair("4", "4"),
        Pair("5", "5"),
        Pair("6", "6"),
        Pair("7", "7"),
        Pair("8", "8"),
        Pair("9", "9")
    ).map {
        Pair(it.first, "C:\\Users\\YourFrog\\Documents\\Tibia\\Numbers\\${it.second}.png")
    }.map {
        val file = File(it.second)
        val image = ImageIO.read(file)

        Pair(it.first, image)
    }

    private val lettersBigger = arrayOf(
        Pair("A", "A"),
        Pair("B", "B"),
        Pair("C", "C"),
        Pair("D", "D"),
        Pair("E", "E"),
        Pair("F", "F"),
        Pair("G", "G"),
        Pair("H", "H"),
        Pair("I", "I"),
        Pair("J", "J"),
        Pair("K", "K"),
        Pair("L", "L"),
        Pair("M", "M"),
        Pair("N", "N"),
//        Pair("O", "O"),
//        Pair("Q", "Q"),
        Pair("P", "P"),
        Pair("R", "R"),
        Pair("S", "S"),
        Pair("T", "T"),
        Pair("U", "U"),
        Pair("V", "V"),
//        Pair("X", "X"),
        Pair("Y", "Y"),
        Pair("Z", "Z")
    ).map {
        Pair(it.first, "C:\\Users\\YourFrog\\Documents\\Tibia\\Letters\\Bigger\\${it.second}.png")
    }.map {
        val file = File(it.second)
        val image = ImageIO.read(file)

        Pair(it.first, image)
    }


    private val lettersLower = arrayOf(
        Pair("a", "a"),
        Pair("b", "b"),
        Pair("c", "c"),
        Pair("d", "d"),
        Pair("e", "e"),
        Pair("f", "f"),
        Pair("g", "g"),
        Pair("h", "h"),
        Pair("i", "i"),
        Pair("j", "j"),
        Pair("k", "k"),
        Pair("l", "l"),
        Pair("m", "m"),
        Pair("n", "n"),
        Pair("o", "o"),
//        Pair("q", "q"),
        Pair("p", "p"),
        Pair("r", "r"),
        Pair("s", "s"),
        Pair("t", "t"),
        Pair("u", "u"),
        Pair("v", "v"),
        Pair("w", "w"),
        Pair("x", "x"),
        Pair("y", "y"),
        Pair("z", "z")
    ).map {
        Pair(it.first, "C:\\Users\\YourFrog\\Documents\\Tibia\\Letters\\Lower\\${it.second}.png")
    }.map {
        //        System.out.println(it.second)

        val file = File(it.second)
        val image = ImageIO.read(file)

        Pair(it.first, image)
    }

    /**
     *  Zwraca litery z których może składać się nazwa istoty
     */
    fun letterOfCreatureName(): List<Pair<String, BufferedImage>> {
        return lettersLower.union(lettersBigger).toList()
    }

    /**
     *  Zwraca litery wykorzystywane tylko w liczbach
     */
    fun letterOfNumbers(): List<Pair<String, BufferedImage>> {
        return numbers
    }
}