import com.soywiz.klogger.Logger
import com.soywiz.korge.Korge
import com.soywiz.korge.view.addHrUpdater
import com.soywiz.korim.bitmap.extract
import com.soywiz.korim.color.Colors
import gameWorld.GameRenderer
import gameWorld.GameWorld
import helper.AssetLoader
import kotlin.math.roundToInt

private const val initialMetricsMultiplier = 3

val log = Logger("main")

suspend fun main() = Korge(
        width = GameWorld.gameVirtualWidth * initialMetricsMultiplier,
        height = GameWorld.gameVirtualHeight * initialMetricsMultiplier,
        virtualWidth = GameWorld.gameVirtualWidth,
        virtualHeight = GameWorld.gameVirtualHeight,
        bgcolor = Colors["#2b2b2b"],
        title = "KorGE Zombie Bird"
) {
    Logger.defaultLevel = Logger.Level.DEBUG

    AssetLoader.load()

    views.gameWindow.icon = AssetLoader.birdMid.extract()

    val world = GameWorld()
    val renderer = GameRenderer(world)

    val input = views.input

    addHrUpdater { delta ->
        if (input.mouseButtons != 0) {  // todo: this algorithm thinks that a long click is multiple clicks
            world.onClick()
        }

        log.debug { "FPS: ${(1 / delta.secondsDouble).roundToInt()}" }

        world.update(delta)
        renderer.render(this)
    }
}
