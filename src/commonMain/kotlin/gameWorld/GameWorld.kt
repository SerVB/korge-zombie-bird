package gameWorld

import com.soywiz.klock.hr.HRTimeSpan
import gameObject.Bird
import gameObject.ScrollHandler

class GameWorld {

    val bird: Bird
    val scroller: ScrollHandler

    init {
        val midPointY = gameVirtualHeight / 2.0

        bird = Bird(33.0 + 17 / 2, midPointY - 5 + 12 / 2, 17, 12)
        scroller = ScrollHandler(midPointY + 66)
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