package ocr_bot

import ocr_bot.reader.MaskOfSpecificColorReader
import ocr_bot.reader.MinimapReader
import ocr_bot.screen.ScreenInterface
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

typealias MaskOfCreatureName = List<Point>

/**
 *  Klasa zajmująca się odczytywaniem potworów z battle listy
 */
class BattleListReader (
    private val baseLeft: Int,
    private val baseTop: Int,
    private val recorder: ScreenInterface,
    private val letterManager: LetterManager
) {
    /**
     *  Kolor w jakim przestawiany jest tekst w sekcji
     */
    private val textColor = Color(192, 192, 192, 255)

    private val highlightTextColor = Color(247,247,247, 255)

    /**
     *  Kolor zaznaczenia potwora
     */
    private val targetColor = Color.RED

    private val highlightTargetColor = Color(255, 128, 128, 255)

    /**
     *  Odnalezione wcześniej istoty
     */
    private val knownledgeCreatures = mutableListOf<KnownledgeCreature>()

    /**
     *  Odczytuje wszystkie kreatury znajdujące się w podanej sekcji
     */
    fun readCreatures(): List<Creature> {
        val creatures = mutableListOf<Creature>()

        for(i in 0 .. 10) {
            val creature = readCreature(i)

            if( creature == null ) {
                // No more creatures
                break
            }

            creatures.add(creature)
        }

        return creatures
    }

    private fun readCreature(positionIndex: Int): Creature? {
        val creatureIsNotExists = isExistsCreatureOnBattleList(positionIndex).not()

        if( creatureIsNotExists ) {
            return null
        }

        return Creature(
            name = getCreatureName(positionIndex),
            healthPercent = readCreaturePercentHealth(positionIndex), //: Int,
            isTarget = isTargetCreature(positionIndex), //: Boolean,
            screenLocation = null //: Point?
        )// getCreatureName(positionIndex)
    }

    /**
     *  Sprawdzenie czy istota na podanej lokalizacji jest zaznaczona
     */
    private fun isTargetCreature(rowNumber: Int): Boolean {
        val color = Color(recorder.capture().getRGB(baseLeft - 2, baseTop + rowNumber * HEIGHT_OF_SINGLE_ROW), true)

        return color == targetColor || color == highlightTargetColor
    }

    /**
     *  Sprawdzenie czy na podanym wierszu znajduje się jakaś istota
     */
    private fun isExistsCreatureOnBattleList(rowNumber: Int): Boolean {
        // @TODO pobierać z całego ekranu nie z sektora bo bez sensu wycinam obrazek
//        val image = recorder.capture(sector)

        val image = clipHpBar(rowNumber)

        // Check border over hp bar
        val leftTop = image.getRGB(0, 0)
        val rightTop = image.getRGB(image.width - 1, 0)
        val rightBottom = image.getRGB(image.width - 1, image.height - 1)
        val leftBottom = image.getRGB(0, image.height - 1)

        return leftTop == rightTop && rightBottom == leftBottom && leftTop == rightBottom
    }

    private fun getCreatureName(rowNumber: Int): String {
        val imageOfCreatureName = clipCreatureName(rowNumber)
        val maskOfCreatureName = makeMaskOfCreatureName(imageOfCreatureName)

        if( maskOfCreatureName.isEmpty() ) {
            ImageIO.write(imageOfCreatureName, "png", File("C:\\Users\\YourFrog\\Documents\\Tibia\\debug.png"))

            val i = 0
        }
        val creatureFromKnownledgeCreature = getCreatureNameFromKnownledge(maskOfCreatureName)

         return when {
            creatureFromKnownledgeCreature == null -> {
                val creatureName = getCreatureNameFromMask(imageOfCreatureName, maskOfCreatureName)

                knownledgeCreatures.add(
                    KnownledgeCreature(
                        name = creatureName,
                        mask = maskOfCreatureName
                ))

                creatureName
            }
            else -> creatureFromKnownledgeCreature
        }
    }

    /**
     *  Utworzenie imienia istoty na podstawie maski
     */
    private fun getCreatureNameFromMask(imageOfCreatureName: BufferedImage, maskOfCreatureName: MaskOfCreatureName): String {
        val pieceOfName = letterManager.letterOfCreatureName().map { (letter, imageOfLetter) ->
            val maskOfLetter = ImageWithPerfectMask(imageOfLetter).let {
                it.mask
            }

            val foundLetterAtPosition = mutableListOf<FoundLetter>()

            for(x in 0 .. imageOfCreatureName.width - imageOfLetter.width) {
                for(y in 0 .. imageOfCreatureName.height - imageOfLetter.height) {
                    var positionHasLetter = true

                    for(entity in maskOfLetter) {
                        val pointOfLetterMask = entity.key

                        val point = Point(pointOfLetterMask.x + x, pointOfLetterMask.y + y)

                        if( !maskOfCreatureName.contains(point) ) {
                            positionHasLetter = false
                            break
                        }
                    }

                    if( positionHasLetter ) {
                       foundLetterAtPosition.add(
                           FoundLetter(
                           letter = letter.toCharArray().first(),
                           leftCorner = Point(x, y),
                           coveringScore = maskOfLetter.size,
                           imageOfLetter = imageOfLetter
                       ))
                    }
                }
            }

            foundLetterAtPosition
//            val i = 0
        }
        .filter {
            it.isNotEmpty() // Tylko odnalezione litery
        }
        .flatten()
        .sortedBy {
            it.leftCorner.x // Sortujemy litery w kolejności odczytu od lewej
        }
        .let {
            removeWrongLetterInCreatureName(it)
        }
        .let {
            buildWords(it)
        }

        return pieceOfName.joinToString(" ")
    }

    /**
     *  Usuwa litery które zostały odnalezione w innych literach ;/ np "c" w "o"
     */
    private fun removeWrongLetterInCreatureName(arr: List<FoundLetter>): List<FoundLetter> {
        val result = mutableListOf<FoundLetter>()

        arr.forEach { candidate ->
            val current = result.firstOrNull {
                //it.leftCorner == candidate.leftCorner ||
                val minX = it.leftCorner.x
                val maxX = it.leftCorner.x + it.imageOfLetter.width + calculateModifierMarginForLetter(null, it.letter) * -1

                when {
                    candidate.leftCorner.x == it.leftCorner.x -> true
                    else -> {
                        candidate.leftCorner.x in minX .. maxX &&
                        candidate.leftCorner.x + candidate.imageOfLetter.width in minX .. maxX
                    }
                }
            }

            if( current == null ) {
                 // Nie odnaleziono litery na tych współrzędnych
                result.add(candidate)
            } else {
                // Odnalazłem litere na tych pozycjach
                if( current.coveringScore < candidate.coveringScore ) {
                    val index = result.indexOf(current)
                    result.set(index, candidate)
                }
            }
        }

        return result
    }

    /**
     *  Próba dopasowania istoty z tych już wcześniej widzianych
     */
    private fun getCreatureNameFromKnownledge(mask: MaskOfCreatureName): String? {
        val foundRememberCreature = knownledgeCreatures.filter {
            // Sprawdzamy zgodność z wielkością maski
            it.mask.size == mask.size
        }.filter {
            // Sprawdzamy zgodność samej maski
            it.mask.forEachIndexed { index, point ->
                val currentMaskPoint = mask.getOrNull(index) ?: return@filter false

                currentMaskPoint.equals(point)
            }

            true
        }

        return when {
            foundRememberCreature.isEmpty() -> null
            foundRememberCreature.size == 1 -> foundRememberCreature.first().name
            else -> {
                // Mamy więcej niż jednego kandydata ;/
                val i = 0

                null
            }
        }
    }

    /**
     *  Tworzy listę punktów gdzie znajdują się kolory związane z literami
     */
    private fun makeMaskOfCreatureName(image: BufferedImage): MaskOfCreatureName {
        val maskReader = MaskOfSpecificColorReader()

        val normalText = maskReader.mask(image, textColor)

        if( normalText.isEmpty() ) {
            return maskReader.mask(image, highlightTextColor)
        }

        return normalText
    }

    /**
     *  Wycina pasek HP istoty
     */
    private fun clipHpBar(rowNumber: Int): BufferedImage {
        val offsetTop = rowNumber * HEIGHT_OF_SINGLE_ROW
        val calculateTop = offsetTop + baseTop

        val sector = Rectangle(baseLeft + 1, calculateTop + HEIGHT_OF_CREATURE_NAME_SPACE , WIDTH_OF_SINGLE_ROW - 1, HEIGHT_OF_CREATURE_HP_BAR_SPACE)

        return recorder.capture(sector)
    }

    /**
     *  Wycina miejsce gdzie może znajdować się nazwa istoty
     */
    private fun clipCreatureName(rowNumber: Int): BufferedImage {
        val offsetTop = rowNumber * HEIGHT_OF_SINGLE_ROW
        val calculateTop = offsetTop + baseTop

        val sector = Rectangle(baseLeft, calculateTop, WIDTH_OF_SINGLE_ROW, HEIGHT_OF_CREATURE_NAME_SPACE)

        return recorder.capture(sector)
    }

    /**
     *  Wyliczenie mofygikacji marginesu dla liter (niektóre litery jak np "t" w foncie tibi nie posiadają marginesu z prawej)
     */
    private fun calculateModifierMarginForLetter(beforeLetter: Char?, currentLetter: Char): Int {
        return when {
            beforeLetter == 'T' && currentLetter == 'i' -> 1
            beforeLetter == 'T' && currentLetter == 'h' -> 1
            beforeLetter == 'L' && currentLetter == 'o' -> 1
            beforeLetter == 'L' && currentLetter == 'i' -> 1
//            beforeLetter == 'R' && currentLetter == 'a' -> 1
//            beforeLetter == 'R' && currentLetter == 'o' -> 1
            beforeLetter == 'R' -> 1
            beforeLetter == 'S' && currentLetter == 't' -> 1
            beforeLetter == 'a' && currentLetter == 'j' -> 1
            beforeLetter == 'a' && currentLetter == 't' -> 1
            beforeLetter == 'a' && currentLetter == 'f' -> 1
            beforeLetter == 'f' && currentLetter == 'i' -> 1
            beforeLetter == 'n' && currentLetter == 'j' -> 1
            beforeLetter == 'l' && currentLetter == 'f' -> 1
//            beforeLetter == 'r' && currentLetter == 'd' -> 1
//            beforeLetter == 'r' && currentLetter == 'a' -> 1
//            beforeLetter == 'r' && currentLetter == 's' -> 1
//            beforeLetter == 'r' && currentLetter == 'r' -> 1
//            beforeLetter == 'r' && currentLetter == 'e' -> 1
//            beforeLetter == 'r' && currentLetter == 'i' -> 1
//            beforeLetter == 'r' && currentLetter == 'm' -> 1
            beforeLetter == 'r' -> 1
            beforeLetter == 't' && currentLetter == 'a' -> 1
            beforeLetter == 't' && currentLetter == 'u' -> 1
            beforeLetter == 't' && currentLetter == 'c' -> 1
            beforeLetter == 'o' && currentLetter == 't' -> 1
            beforeLetter == 'u' && currentLetter == 't' -> 1
            beforeLetter == 's' && currentLetter == 't' -> 1
            beforeLetter == null && currentLetter == 't' -> 1
            else -> 0
        }
    }

    private fun buildWords(map: List<FoundLetter>): List<String> {
        val digits = mutableListOf<String>()

        val func: (Int, Int) -> Unit = { first, last ->
            val value = (first .. last).map {
                map[it].letter
            }.joinToString("")

            digits.add(value)
        }

        if( map.isEmpty() ) {
            val i = 0
        }

        var first = 0
        var last = map[0]

        for(i in 1 until map.size) {
            val current = map[i]

//            val lastTag = last.third as Pair<String, BufferedImage>
//            val currentTag = current.third as Pair<String, BufferedImage>

            val currentLetter = current.letter// currentTag.first
            val lastLetter = last.letter

            val modifier = calculateModifierMarginForLetter(lastLetter, currentLetter)

            if( current.leftCorner.x + modifier == last.leftCorner.x + last.imageOfLetter.width ) {
                last = current
            } else {
                func(first, i - 1)

                first = i
                last = current
            }
        }

        func(first, map.size - 1)

        return digits
    }

    private fun readCreaturePercentHealth(rowNumber: Int): Int {
        val image = clipHpBar(rowNumber)

        val barColor = image.getRGB(1, 2)

        for(i in 1 .. image.width - 2) {
            val sampleOfBarColor = image.getRGB(1 + i, 2)

            if( sampleOfBarColor != barColor ) {
                val percent = ((i / (image.width - 2).toFloat()) * 100).toInt()
                return percent
            }
        }

        return 100
    }

    companion object {
        const val HEIGHT_OF_SINGLE_ROW = 22
        const val WIDTH_OF_SINGLE_ROW = 133
        const val HEIGHT_OF_CREATURE_NAME_SPACE = 12
        const val HEIGHT_OF_CREATURE_HP_BAR_SPACE = 5
    }


    private data class FoundLetter (
        val letter: Char,
        val leftCorner: Point,
        val coveringScore: Int,
        val imageOfLetter: BufferedImage
    )

    private data class KnownledgeCreature(
        /**
         *  Imie istoty
         */
        val name: String,

        /**
         *  Maska imienia
         */
        val mask: MaskOfCreatureName
    )
}