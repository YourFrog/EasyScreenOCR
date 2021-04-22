import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileWriter
import java.io.RandomAccessFile
import javax.imageio.ImageIO
import kotlin.math.max


fun main() {
    val screen = Toolkit.getDefaultToolkit().screenSize
    val area = Rectangle(screen)


    val reader = Reader()

    var lastMana = Mana(0, 0)
    var lastHealth = Health(0, 0)

    while( true ) {
        reader.renewCapture()

//
//        reader.readMana()?.let { mana ->
//            if( lastMana != mana ) {
//                val content = mana.current.toString() + " " + mana.max.toString()
//
//                val writer = FileWriter("I:/mana.txt")
//                writer.write(content)
//                writer.flush()
//
//                lastMana = mana
//            }
//        }
//
//        reader.readHealth()?.let { health ->
//            if( lastHealth != health ) {
//                val content = health.current.toString() + " " + health.max.toString()
//
//                val writer = FileWriter("I:/health.txt")
//                writer.write(content)
//                writer.flush()
//
//                lastHealth = health
//            }
//        }

        val client = benchmark {
            Client(
                health = reader.readHealth(),
                mana = reader.readMana(),
                friends = reader.readFriends(),
                isHungry = reader.readIsHungry()
            )
        }

//
//        client.health?.let { health ->
//            val content = "" + health.current + " " + health.max
//            val writer = FileWriter("I:/health.txt")
//            writer.write(content)
//            writer.flush()
//        }
//
//        client.mana?.let { mana ->
//            val content = "" + mana.current + " " + mana.max
//            val writer = FileWriter("I:/mana.txt")
//            writer.write(content)
//            writer.flush()
//        }
//
//        client.friends?.let { friends ->
//            val content = "" + friends.size
//            val writer = FileWriter("I:/friends.txt")
//            writer.write(content)
//            writer.flush()
//        }
        val i = 0
    }
}

class Reader {
    private lateinit var screenCapture: BufferedImage

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
//        Pair("G", "G"),
//        Pair("H", "H"),
        Pair("I", "I"),
        Pair("J", "J"),
        Pair("K", "K"),
        Pair("L", "L"),
        Pair("M", "M"),
        Pair("N", "N"),
//        Pair("O", "O"),
        Pair("Q", "Q"),
//        Pair("P", "P"),
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
        System.out.println(it.second)
        val file = File(it.second)
        val image = ImageIO.read(file)

        Pair(it.first, image)
    }

    private val hungry = ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Status\\hungry.png"))
    private val fight = ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Status\\fight.png"))

    private val lettersLower = arrayOf(
        Pair("a", "a"),
        Pair("b", "b"),
        Pair("c", "c"),
        Pair("d", "d"),
        Pair("e", "e"),
        Pair("f", "f"),
//        Pair("g", "g"),
        Pair("h", "h"),
        Pair("i", "i"),
        Pair("j", "j"),
        Pair("k", "k"),
        Pair("l", "l"),
        Pair("m", "m"),
        Pair("n", "n"),
//        Pair("o", "o"),
//        Pair("q", "q"),
        Pair("p", "p"),
        Pair("r", "r"),
        Pair("s", "s"),
        Pair("t", "t"),
        Pair("u", "u"),
        Pair("v", "v"),
//        Pair("x", "x"),
        Pair("y", "y"),
        Pair("z", "z")
    ).map {
        Pair(it.first, "C:\\Users\\YourFrog\\Documents\\Tibia\\Letters\\Lower\\${it.second}.png")
    }.map {
        System.out.println(it.second)

        val file = File(it.second)
        val image = ImageIO.read(file)

        Pair(it.first, image)
    }

    private val robot: Robot by lazy { Robot() }

    init {
        renewCapture()
    }

    fun readFriends(): List<Creature> {
        // 132 / 5 -> HP Bar size, 1px border => 130 / 3
        // 1582, 215

        // 31, 80

        val friends = mutableListOf<Creature>()

        for(i in 0 .. 5) {
            val baseTop = 85
            val offsetTop = 22 * i

            val sector = Rectangle(1594, offsetTop + baseTop + 12 , 132, 5)
            val isExists = isExistsCreatureOnBattleList(sector)

            if( !isExists ) {
                break
            }

            val data = parseScreen(
                sector = Rectangle(1594, offsetTop + baseTop, 132, 12),
                numbers = lettersLower.union(lettersBigger).toList()
            ).firstOrNull {
                it.isNotEmpty()
            }?.let { name ->
                friends.add(Creature(
                    name = name,
                    healthPercent = readCreaturePercentHealth(sector)
                ))
            }

        }

        return friends
    }

    fun readIsHungry(): Boolean {
        return findSameImage(
            source = captureOfStatusBar(),
            target = hungry
        )
    }

    fun readIsFight(): Boolean {

    }

    private fun captureOfStatusBar(): BufferedImage {
        return capture(
            Rectangle(1753, 283, 106, 11)
        )
    }

    fun isExistsCreatureOnBattleList(sector: Rectangle): Boolean {
        val image = capture(sector)

        // Check border over hp bar
        val leftTop = image.getRGB(0, 0)
        val rightTop = image.getRGB(image.width - 1, 0)
        val rightBottom = image.getRGB(image.width - 1, image.height - 1)
        val leftBottom = image.getRGB(0, image.height - 1)

        return leftTop == rightTop && rightBottom == leftBottom && leftTop == rightBottom
    }

    fun readCreaturePercentHealth(sector: Rectangle): Int {
        val image = capture(sector)

        val barColor = image.getRGB(1, 2)

        for(i in 1 .. sector.width - 2) {
            val sampleOfBarColor = image.getRGB(1 + i, 2)

            if( sampleOfBarColor != barColor ) {
                val percent = ((i / (sector.width - 2).toFloat()) * 100).toInt()
                return percent
            }
        }

        return 100
    }

    fun readHealth(): Health? {
        val data = parseScreen(
            sector = Rectangle(188, 25, 868, 10),
            numbers = numbers
        )

        return if( data.isEmpty() ) {
            null
        } else {
            Health(
                current = data[0].toInt(),
                max = data[1].toInt()
            )
        }
    }

    fun readMana(): Mana? {
        val data = parseScreen(
            sector = Rectangle(876, 25, 679, 10),
            numbers = numbers
        )

        return if( data.isEmpty() ) {
            null
        } else {
            Mana(
                current = data[0].toInt(),
                max = data[1].toInt()
            )
        }
    }

    private fun capture(sector: Rectangle): BufferedImage
    {
        val capture = screenCapture.getSubimage(sector.x, sector.y, sector.width, sector.height)

        ImageIO.write(capture, "png", File("C:\\Users\\YourFrog\\Documents\\Tibia\\output.png"))

        return capture
    }

    fun renewCapture() {
        screenCapture = ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Test-friends-2-rat.png"))
//        val screen = Toolkit.getDefaultToolkit().screenSize
//        val area = Rectangle(screen)
//
//        screenCapture = robot.createScreenCapture(area)
    }

    private fun parseScreen(sector: Rectangle, numbers: List<Pair<String, BufferedImage>>): List<String> {
        val capture = capture(sector)

        return listOf(
            Pair(
                sector,
                numbers
            )
        ).map {
            // Start: clip

            Pair(capture, it.second)
        }.map { (clipImage, numbers) ->

            numbers.map { (number, numberImage) ->
                val numberColors = Array(numberImage.height) {
                    Array<Color?>(numberImage.width) { null }
                }

                for (cx in 0 until numberImage.width) {
                    for (cy in 0 until numberImage.height) {
                        val pixel = numberImage.getRGB(cx, cy)

                        Color(pixel, true).takeIf {
                            it.alpha != 0
                        }?.let {
                            numberColors[cy][cx] = it
                        }
                    }
                }

//                benchmark("Parse $number") {
                    findImage(numberColors, clipImage, Pair(number, numberImage))
//                }
            }.flatten()
        }.map {

            it.forEach { (x, y, tag) ->
                val image = (tag as Pair<String, BufferedImage>).second

                capture.graphics.color = Color.RED
                capture.graphics.drawRect(x, y, image.width, image.height)
            }

            it
        }
        .flatten()
        .sortedBy { it.first }
        .groupBy {
            it.second
        }.map { map ->
            val digits = mutableListOf<String>()

            val func: (Int, Int) -> Unit = { first, last ->
                val value = (first .. last).map {
                    (map.value[it].third as Pair<String, BufferedImage>).first
                }.joinToString("")

                digits.add(value)
            }

            var first = 0
            var last = map.value[0]

            for(i in 1 until map.value.size) {
                val current = map.value[i]

                val tag = last.third as Pair<String, BufferedImage>
                val currentTag = current.third as Pair<String, BufferedImage>

                val modifier = when(currentTag.first) {
                    "t",
                    "j" -> 1
                    else -> 0
                } + when(tag.first) {
                    "R" -> 1
                    else -> 0
                }


                if( current.first + modifier == last.first + tag.second.width ) {
                    last = current
                } else {
                    func(first, i - 1)

                    first = i
                    last = current
                }
            }

            func(first, map.value.size - 1)
            val i = 0

            digits
        }.flatten()
    }
}

fun findSameImage(source: BufferedImage, target: BufferedImage): Boolean {

    val mapOfMask: Map<Point, Color> = target.let {
        val result = mutableMapOf<Point, Color>()

        for(x in 0 until it.width) {
            for(y in 0 until it.height) {
                val point = Point(x, y)
                val color = Color(it.getRGB(x, y), true)

                if( color.alpha == 255 ) {
                    result[point] = color
                }
            }
        }

        result
    }

    val isFoundSameImage: (Int, Int, Map<Point, Color>) -> Boolean = { x, y, mask ->
        var isFound = true
        var samePoints = 0

        for(entity in mask) {
            val point = Point(entity.key.x + x, entity.key.y + y)
            val color = Color(source.getRGB(point.x, point.y), true)

            if( color.rgb != entity.value.rgb ) {
                isFound = false
                break
            }

            samePoints++
        }

        isFound
    }

    for(x in 0 .. source.width - target.width) {
        for(y in 0 .. source.height - target.height) {

            if( isFoundSameImage(x, y, mapOfMask) ) {
                return true
            }
        }
    }

    return false
}

fun findImage(numberColors: Array<Array<Color?>>, image: BufferedImage, tag: Any): List<Triple<Int, Int, Any>> {
    val result = mutableListOf<Triple<Int, Int, Any>>()

    val width = numberColors[0].size
    val height = numberColors.size

    val primaryColors = numberColors.mapIndexed { cy, items ->
        items.mapIndexed { cx, pixel ->
            if( pixel == null ) {
                null
            } else {
                Pair(cy, cx)
            }
        }
    }
    .flatten()
    .filterNotNull()

    val primaryColor = primaryColors.first()
    val perfectScore = width * height

    var maxFound = 0

    for(x in 0 .. image.width - width) {
        for(y in 0 .. image.height - height) {

            var found = 0
            var capturePrimary: Color = Color(image.getRGB(x + primaryColor!!.second, y + primaryColor!!.first), true)



            numberColors.forEachIndexed isFound@{ cy, crow ->
                crow.forEachIndexed { cx, cPixel ->
                    val pixel = image.getRGB(x + cx, y + cy)
                    val color = Color(pixel, true)

                    when {
                        cPixel == null && capturePrimary == null -> found++
                        cPixel == null && capturePrimary != null && color.rgb != capturePrimary!!.rgb -> found++
                        cPixel != null && capturePrimary == null -> {
                            found++
                            capturePrimary = color
                        }
                        cPixel != null && color.rgb == capturePrimary!!.rgb -> {
                            found++
                        }
                        else -> return@isFound
                    }
                }
            }

            maxFound = max(maxFound, found)

            if( found == perfectScore ) {
                result.add(Triple(x, y, tag))

                y + image.height
            }
        }
    }

    return result
}

fun <T> benchmark(name: String = "unknown", block: () -> T): T {
    log("Start - $name")
    val startTime = System.nanoTime()

    val result = block()

    val finishTime = System.nanoTime()
    val ms = (finishTime - startTime) / 1000000

    log("Finish - $name, Time: " + ms + " ms")

    return result
}

fun log(message: String) {
    val time = System.nanoTime()

    System.out.println("[$time] " + message)
}

data class Mana(
    val current: Int,
    val max: Int
)

data class Health(
    val current: Int,
    val max: Int
)

data class Creature(
    val name: String,
    val healthPercent: Int
)

data class Client(
    val health: Health?,
    val mana: Mana?,
    val friends: List<Creature>,
    val isHungry: Boolean
) {
    enum class CharacterStatus {
        HUNGRY,
        FIGHT
    }
}