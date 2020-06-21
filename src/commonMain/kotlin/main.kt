import com.soywiz.korge.Korge
import com.soywiz.korge.view.addHrUpdater
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.text
import com.soywiz.korim.bitmap.extract
import com.soywiz.korim.color.Colors
import game.GameWorld
import game.gameWorld
import helper.AssetLoader
import kotlin.math.roundToInt

private const val initialMetricsMultiplier = 3

suspend fun main() = Korge(
        width = GameWorld.gameVirtualWidth * initialMetricsMultiplier,
        height = GameWorld.gameVirtualHeight * initialMetricsMultiplier,
        virtualWidth = GameWorld.gameVirtualWidth,
        virtualHeight = GameWorld.gameVirtualHeight,
        bgcolor = Colors["#2b2b2b"],
        title = "KorGE Zombie Bird"
) {
    AssetLoader.load()

    views.gameWindow.icon = AssetLoader.birdMid.extract()

    val world = gameWorld()

    val fpsText = text("FPS: ...")
            .apply { filtering = false }
            .alignBottomToBottomOf(this)

    val input = views.input

    addHrUpdater { delta ->
        if (input.mouseButtons != 0) {  // todo: this algorithm thinks that a long click is multiple clicks
            world.onClick()
        }

        fpsText.text = "FPS: ${(1 / delta.secondsDouble).roundToInt()}"

        world.update(delta)
    }
}
