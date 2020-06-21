package game

import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo

fun Container.gameWorld() = GameWorld().addTo(this)

class GameWorld : Container() {

    private val backgroundView: BackgroundView
    private val scroller: ScrollHandler
    private val bird: Bird

    init {
        val midPointY = gameVirtualHeight / 2.0

        backgroundView = backgroundView(midPointY)
        scroller = scrollHandler(midPointY + 66)
        bird = bird(33.0 + 17 / 2, midPointY - 5 + 12 / 2)
    }

    fun update(delta: HRTimeSpan) {
        bird.update(delta)
        scroller.update(delta)

        if (scroller.collides(bird)) {
            scroller.stop()
        }
    }

    fun onClick() {
        bird.onClick()
    }

    companion object {

        const val gameVirtualWidth = 136
        const val gameVirtualHeight = 204
    }
}