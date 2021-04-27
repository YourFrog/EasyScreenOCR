package ocr_bot

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.ptr.PointerByReference
import ocr_bot.reader.MaskOfSpecificColorReader
import ocr_bot.screen.FileDebuggerScreen
import ocr_bot.screen.RealtimeScreen
import ocr_bot.screen.ScreenInterface
import ocr_bot.script.*
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.floor
import kotlin.math.max

typealias ImagePerfectMask = Map<Point, Color>

const val IS_DEBUG = false
const val DEBUG_OUTPUT = false
//const val DEBUG_SCREEN = "one-rat"
const val DEBUG_SCREEN = "eremo_island"


class ImageWithPerfectMask(
    val image: BufferedImage
) {
    val mask: ImagePerfectMask by lazy {
        createPerfectMask(image)
    }

    fun createPerfectMask(image: BufferedImage): ImagePerfectMask {
        val result = mutableMapOf<Point, Color>()

        for(x in 0 until image.width) {
            for(y in 0 until image.height) {
                val point = Point(x, y)
                val color = Color(image.getRGB(x, y), true)

                if( color.alpha == 255 ) {
                    result[point] = color
                }
            }
        }

        return result
    }
}

internal object Psapi {
    external fun GetModuleBaseNameW(hProcess: Pointer?, hmodule: Pointer?, lpBaseName: CharArray?, size: Int): Int

    init {
        Native.register("psapi")
    }
}

internal object Kernel32 {
    var PROCESS_QUERY_INFORMATION = 0x0400
    var PROCESS_VM_READ = 0x0010
    external fun GetLastError(): Int
    external fun OpenProcess(dwDesiredAccess: Int, bInheritHandle: Boolean, pointer: Pointer?): Pointer?

    init {
        Native.register("kernel32")
    }
}

internal object User32DLL {
    external fun GetWindowThreadProcessId(hWnd: HWND?, pref: PointerByReference?): Int
    external fun GetForegroundWindow(): HWND?
    external fun GetWindowTextW(hWnd: HWND?, lpString: CharArray?, nMaxCount: Int): Int

    init {
        Native.register("user32")
    }
}

private const val MAX_TITLE_LENGTH = 1024
fun main() {
    val scripts: List<ScriptInterface> = listOf(
        AutoHealing(),
//        AutoAttack(
//            ignoreMonsters = listOf(
//                ScriptInterface.Monster.DOG
//            )
//        ),
//        AutoTargetMonsterScript()
//        AutoSioScript(supportCreatureWithName = "Kalan"),
        AutoManaPotion(potion = ScriptInterface.Item.GREAT_MANA_POTION),
//        AutoHasteScript(),
//        AutoCurseScript(),
//        ManaBurningScript(spell = ScriptInterface.Spell.UTANA_VID),
        AutoEat()
    )

    val screen = Toolkit.getDefaultToolkit().screenSize
    val area = Rectangle(screen)


    val reader = Reader()

    var lastMana = Mana(0, 0)
    var lastHealth = Health(0, 0)

    val keyboardRobot = Robot()
    var lastVisible = false

    var lastTime = System.currentTimeMillis();

    while( true ) {

        val currentTime = System.currentTimeMillis()

        if( currentTime < lastTime + 1000) {
            continue
        }

        lastTime = System.currentTimeMillis()

        if( !IS_DEBUG ) {
            val buffer = CharArray(MAX_TITLE_LENGTH * 2)
            User32DLL.GetWindowTextW(User32DLL.GetForegroundWindow(), buffer, MAX_TITLE_LENGTH)

            val windowTitle = String(buffer)

            if (!windowTitle.startsWith("Tibia -")) {
                lastVisible = false
                continue
            }

            if (!lastVisible) {
                lastVisible = true
                Thread.sleep(2000)
            }
        }

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

//        reader.readEnemies()

        val client = benchmark {
            Client(
                keyboard = keyboardRobot,
                health = reader.readHealth(),
                mana = reader.readMana(),
                friends = emptyList(), //reader.readFriends(),
                enemies = emptyList(), //reader.readEnemies(),
                isHungry = reader.readIsHungry(),
                isFight = reader.readIsFight(),
                isPoison = reader.readIsPoison(),
                isHaste = reader.readIsHaste(),
                isBleeding = reader.readIsBleeding(),
                isUtamo = reader.readIsUtamo(),
                isHealingCooldown = reader.readIsHealingCooldown(),
                isAttackCooldown = reader.readIsAttackCooldown(),
                isSupportCooldown = reader.readIsSupportCooldown()
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

        scripts.forEach { script ->
            script.execute(client)
        }
    }
}

class Reader {
    /**
     *  Manager odpowiedzialny za przechowywanie literek
     */
    private val letterManager = LetterManager()

    /**
     *  Nagrywarka ekranu
     */
    private val recorder: ScreenInterface = if( IS_DEBUG && DEBUG_SCREEN.isNotEmpty() ) {
        FileDebuggerScreen("C:\\Users\\YourFrog\\Documents\\Tibia\\Test\\" + DEBUG_SCREEN + ".png")
    } else {
        RealtimeScreen()
    }

    private val readerOfEnemiesBattleList = BattleListReader(
        baseLeft = 1594,
        baseTop = 446,
        recorder = recorder,
        letterManager = LetterManager()
    )

    private val readerOfFriendsBattleList = BattleListReader(
        baseLeft = 1594,
        baseTop = 85,
        recorder = recorder,
        letterManager = LetterManager()
    )

    private val hungry = ImageWithPerfectMask(ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Status\\hungry.png")))
    private val fight = ImageWithPerfectMask(ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Status\\fight.png")))
    private val haste = ImageWithPerfectMask(ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Status\\haste.png")))
    private val bleeding = ImageWithPerfectMask(ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Status\\bleeding.png")))
    private val poison = ImageWithPerfectMask(ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Status\\poison.png")))
    private val utamo = ImageWithPerfectMask(ImageIO.read(File("C:\\Users\\YourFrog\\Documents\\Tibia\\Status\\utamo.png")))

    private val robot: Robot by lazy { Robot() }

    init {
        renewCapture()
    }

    fun readEnemies() = benchmark("read enemies on battle list") {
        readerOfEnemiesBattleList.readCreatures()
    }

    fun readFriends() = benchmark("read friends on battle list") {
        readerOfFriendsBattleList.readCreatures()
    }

    private fun readCreatures(offsetTop: Int): List<Creature> {
        val creatures = mutableListOf<Creature>()
        val baseTop = offsetTop
        val baseLeft = 1594

        val foundCreatures = benchmark("read enemies battle list") {
            readerOfEnemiesBattleList.readCreatures()
        }

        return emptyList()

        for(i in 0 .. 10) {
            val offsetTop = 22 * i
            val calculateTop = offsetTop + baseTop

            val sector = Rectangle(baseLeft, calculateTop + 12 , 132, 5)
            val isExists = isExistsCreatureOnBattleList(sector)

            if( !isExists ) {
                break
            }

            val data = benchmark("read creature") {
                parseScreen(
                    sector = Rectangle(baseLeft, calculateTop, 132, 12),
                    numbers = letterManager.letterOfCreatureName()
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    it.joinToString(separator = " ") { it }.trim()
                }?.let { name ->
                    val isTargetColor = Color(recorder.capture().getRGB(baseLeft - 3, calculateTop), true)

                    System.out.println(name)

                    creatures.add(Creature(
                        name = name,
                        healthPercent = readCreaturePercentHealth(sector),
                        isTarget = isTargetColor == Color.RED,
                        screenLocation = null //searchCreatureOnGameScreen(name)
                    ))
                }
            }
        }

        return creatures

    }

    private val imagesOfCreatureName: MutableMap<String, ImageWithPerfectMask> = mutableMapOf()

    private fun generateImageOfCreatureName(name: String): BufferedImage {
        val allowLetters = letterManager.letterOfCreatureName().filter {
            name.contains(it.first.first())
        }

        val calculateLettersWidth: (List<Pair<String, BufferedImage>>) -> Int = { letters ->
            var width = 0

            letters.forEachIndexed { index, (currentLetter, letterImage) ->
                val beforeLetter = letters.getOrNull(index - 1)?.first ?: ""

                width += when {
                    beforeLetter == "T" && currentLetter == "i" -> -1
                    else -> 0
                }

                width += letterImage.width + when {
                    currentLetter == "r" -> -1
                    else -> 0
                }
            }

            width
        }

        return name.map { charOfName ->
            when(charOfName) {
                ' ' -> Pair(" ", BufferedImage(3, 12, BufferedImage.TYPE_INT_ARGB))
                else -> allowLetters.first {
                    it.first.first() == charOfName
                }
            }

        }.let { letters ->

            val width = calculateLettersWidth(letters)// letters.size * 10
            val height = 12

            val imageOfCreatureName = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val graphics = imageOfCreatureName.createGraphics() as Graphics2D

            graphics.background = Color(0, 0, 0, 0)//Color.WHITE
            graphics.clearRect(0, 0, width, height)

            var offetLeft = 0
            letters.forEachIndexed { index, (currentLetter, letterImage) ->
                val beforeLetter = letters.getOrNull(index - 1)?.first ?: ""

                offetLeft += when {
                    beforeLetter == "T" && currentLetter == "i" -> -1
                    else -> 0
                }

                graphics.drawImage(letterImage, offetLeft, 0, null)

                offetLeft += letterImage.width + when {
                    currentLetter == "r" -> -1
                    else -> 0
                }
            }

            if (IS_DEBUG && DEBUG_OUTPUT) {
                ImageIO.write(
                    imageOfCreatureName,
                    "png",
                    File("C:\\Users\\YourFrog\\Documents\\Tibia\\output-monster-name.png")
                )
            }

            imageOfCreatureName
        }
    }

    private fun searchCreatureOnGameScreen(name: String): Point? {
        val capture = captureOfGameScreen()

        val imageWithPerfectMask = imagesOfCreatureName.getOrPut(name) {
            ImageWithPerfectMask(
                generateImageOfCreatureName(name)
            )
        }

        return imageWithPerfectMask.let{
            benchmark("find creature: $name") {
                findSameImageWithText(capture, it)
            }
        }?.let {
            val square = calculateSquareSize()

            Point(
                (it.x + imageWithPerfectMask.image.width / 2) / square.width,
                (it.y + calculateSquareSize().height / 2) / square.height
            )
        }
    }

    private fun calculateSquareSize(): Dimension {
        return Dimension(
            1056 / Constants.TibiaClient.SCREEN_WIDTH,
            744 / Constants.TibiaClient.SCREEN_HEIGHT
        )
    }

    private fun captureOfGameScreen(): BufferedImage {
        return recorder.capture(
            Rectangle(344, 78, 1056, 744)
        )
    }

    fun readIsHungry(): Boolean = findSameImage(
        source = captureOfStatusBar(),
        perfectMask = hungry
    )

    fun readIsFight(): Boolean = findSameImage(
        source = captureOfStatusBar(),
        perfectMask = fight
    )

    fun readIsHaste(): Boolean = findSameImage(
        source = captureOfStatusBar(),
        perfectMask = haste
    )

    fun readIsBleeding(): Boolean = findSameImage(
        source = captureOfStatusBar(),
        perfectMask = bleeding
    )

    fun readIsPoison(): Boolean = findSameImage(
        source = captureOfStatusBar(),
        perfectMask = poison
    )

    fun readIsUtamo(): Boolean = findSameImage(
        source = captureOfStatusBar(),
        perfectMask = utamo
    )

    fun readIsHealingCooldown(): Boolean {
        // Jeśli jest cooldown na zaklecia leczace to zapalony jest pixel (ostatni który znika z paska)

        val color = Color(recorder.capture().getRGB(206, 888), true)

        return color == Color.WHITE
    }

    fun readIsAttackCooldown(): Boolean {
        // Jeśli jest cooldown na zaatakowanie to zapalony jest pixel (ostatni który znika z paska)
        val color = Color(recorder.capture().getRGB(180, 888), true)

        return color == Color.WHITE
    }

    fun readIsSupportCooldown(): Boolean {
        // Jeśli jest cooldown na zaatakowanie to zapalony jest pixel (ostatni który znika z paska)
        val color = Color(recorder.capture().getRGB(230, 888), true)

        return color == Color.WHITE
    }

    private fun captureOfStatusBar(): BufferedImage {
        return recorder.capture(
            Rectangle(1753, 283, 106, 11)
        )
    }

    private fun isExistsCreatureOnBattleList(sector: Rectangle): Boolean {
        val image = recorder.capture(sector)

        // Check border over hp bar
        val leftTop = image.getRGB(0, 0)
        val rightTop = image.getRGB(image.width - 1, 0)
        val rightBottom = image.getRGB(image.width - 1, image.height - 1)
        val leftBottom = image.getRGB(0, image.height - 1)

        return leftTop == rightTop && rightBottom == leftBottom && leftTop == rightBottom
    }

    private fun readCreaturePercentHealth(sector: Rectangle): Int {
        val image = recorder.capture(sector)

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

    fun readHealth(): Health {
        val data = parseScreen(
            sector = Rectangle(471, 25, 115, 10),
//            sector = Rectangle(876, 25, 679, 10),
            numbers = letterManager.letterOfNumbers()
        )

        return Health(
            current = data[0].toInt(),
            max = data[1].toInt()
        )
    }

    fun readMana(): Mana {
        val data = parseScreen(
            sector = Rectangle(1059, 25, 215, 10),
            numbers = letterManager.letterOfNumbers()
        )

        return Mana(
            current = data[0].toInt(),
            max = data[1].toInt()
        )
    }

    fun renewCapture() {
        recorder.refresh()
    }

    private fun parseScreen(sector: Rectangle, numbers: List<Pair<String, BufferedImage>>): List<String> {
        val capture = recorder.capture(sector)

        return parseScreen(capture, numbers)
    }

    private fun parseScreen(capture: BufferedImage, numbers: List<Pair<String, BufferedImage>>): List<String> {
        return listOf(
            Pair(capture, numbers)
        ).map { (clipImage, numbers) ->
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
        }
        .flatten()
        .sortedBy { it.first }
        .groupBy {
            it.second
        }.map { map ->
            benchmark("build words")                        {
                buildWords(map)
            }
        }.flatten()
    }
}

private fun buildWords(map: Map.Entry<Int, List<Triple<Int, Int, Any>>>): List<String> {

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

        val lastTag = last.third as Pair<String, BufferedImage>
        val currentTag = current.third as Pair<String, BufferedImage>

        val currentLetter = currentTag.first
        val lastLetter = lastTag.first

        val modifier = when {
            lastLetter == "T" && currentLetter == "i" -> 1
            lastLetter == "T" && currentLetter == "h" -> 1
            lastLetter == "L" && currentLetter == "o" -> 1
            lastLetter == "L" && currentLetter == "i" -> 1
            lastLetter == "R" && currentLetter == "a" -> 1
            lastLetter == "S" && currentLetter == "t" -> 1
            lastLetter == "a" && currentLetter == "j" -> 1
            lastLetter == "a" && currentLetter == "t" -> 1
            lastLetter == "n" && currentLetter == "j" -> 1
            lastLetter == "r" && currentLetter == "d" -> 1
            lastLetter == "r" && currentLetter == "a" -> 1
            lastLetter == "r" && currentLetter == "s" -> 1
            lastLetter == "t" && currentLetter == "a" -> 1
            lastLetter == "t" && currentLetter == "u" -> 1
            lastLetter == "t" && currentLetter == "c" -> 1
            lastLetter == "o" && currentLetter == "t" -> 1
            lastLetter == "u" && currentLetter == "t" -> 1
            else -> 0
        }


        if( current.first + modifier == last.first + lastTag.second.width ) {
            last = current
        } else {
            func(first, i - 1)

            first = i
            last = current
        }
    }

    func(first, map.value.size - 1)
    val i = 0

    return digits
}

fun findSameImage(source: BufferedImage, perfectMask: ImageWithPerfectMask): Boolean {
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

    val target = perfectMask.image

    for(x in 0 .. source.width - target.width) {
        for(y in 0 .. source.height - target.height) {

            if( isFoundSameImage(x, y, perfectMask.mask) ) {
                return true
            }
        }
    }

    return false
}

fun findSameImageWithText(source: BufferedImage, perfectMask: ImageWithPerfectMask): Point? {
    val isFoundSameImage: (Int, Int, Map<Point, Color>) -> Boolean = { x, y, mask ->
        var isFound = true
        var samePoints = 0
        val textColor: Color = mask.keys.first().let {
            val rgb = source.getRGB(x + it.x, y + it.y)
            Color(rgb, true)
        }

        for(entity in mask) {
            val point = Point(entity.key.x + x, entity.key.y + y)
            val color = Color(source.getRGB(point.x, point.y), true)

            if( color.rgb == textColor.rgb ) {
                samePoints++
            } else {
                isFound = false
                break
            }
        }

        isFound
    }

    val target = perfectMask.image

    for(y in 0 .. source.height - target.height) {
//        System.out.println("oy: " + y)
        for(x in 0 .. source.width - target.width) {
            if( isFoundSameImage(x, y, perfectMask.mask) ) {
                return Point(x, y)
            }
        }
    }

    return null
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
) {
    fun percent(): Int {
        return floor(current.toFloat() / max * 100).toInt()
    }
}

data class Health(
    val current: Int,
    val max: Int
) {
    fun percent(): Int {
        val pc = floor(current.toFloat() / max * 100).toInt()

        return pc
    }
}

data class Creature(
    val name: String,
    val healthPercent: Int,
    val isTarget: Boolean,
    val screenLocation: Point?
)

data class Client(
    val keyboard: Robot,
    val health: Health,
    val mana: Mana,
    val friends: List<Creature>,
    val enemies: List<Creature>,
    val isHungry: Boolean,
    val isFight: Boolean,
    val isPoison: Boolean,
    val isHaste: Boolean,
    val isBleeding: Boolean,
    val isUtamo: Boolean,
    val isHealingCooldown: Boolean,
    val isAttackCooldown: Boolean,
    val isSupportCooldown: Boolean
) {
    enum class CharacterStatus {
        HUNGRY,
        FIGHT
    }
}