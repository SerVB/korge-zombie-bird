package game

import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo

fun Container.scrollHandler(groundY: Double) = ScrollHandler(groundY).addTo(this)

class ScrollHandler(groundY: Double) : Container() {

    val grass1 = grass(0.0, groundY, SCROLL_SPEED)
    val grass2 = grass(grass1.tailX, groundY, SCROLL_SPEED)

    val pipe1 = pipe(210.0, 0.0, 60, SCROLL_SPEED, groundY)
    val pipe2 = pipe(pipe1.tailX + PIPE_GAP, 0.0, 70, SCROLL_SPEED, groundY)
    val pipe3 = pipe(pipe2.tailX + PIPE_GAP, 0.0, 60, SCROLL_SPEED, groundY)

    fun update(delta: HRTimeSpan) {
        grass1.update(delta)
        grass2.update(delta)
        pipe1.update(delta)
        pipe2.update(delta)
        pipe3.update(delta)

        when {
            pipe1.isScrolledLeft -> pipe1.reset(pipe3.tailX + PIPE_GAP)
            pipe2.isScrolledLeft -> pipe2.reset(pipe1.tailX + PIPE_GAP)
            pipe3.isScrolledLeft -> pipe3.reset(pipe2.tailX + PIPE_GAP)
        }

        when {
            grass1.isScrolledLeft -> grass1.reset(grass2.tailX)
            grass2.isScrolledLeft -> grass2.reset(grass1.tailX)
        }
    }

    fun stop() {
        grass1.stop()
        grass2.stop()
        pipe1.stop()
        pipe2.stop()
        pipe3.stop()
    }

    fun collides(bird: Bird): Boolean {
        return pipe1.collides(bird) || pipe2.collides(bird) || pipe3.collides(bird)
    }

    companion object {

        const val SCROLL_SPEED = -59.0
        const val PIPE_GAP = 49
    }
}