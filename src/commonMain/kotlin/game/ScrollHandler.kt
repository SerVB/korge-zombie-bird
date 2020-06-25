package game

import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.korau.sound.PlaybackParameters
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import helper.AssetLoader

fun Container.scrollHandler(groundY: Double) = ScrollHandler(groundY).addTo(this)

class ScrollHandler(groundY: Double) : Container() {

    private val grass1 = grass(0.0, groundY, SCROLL_SPEED)
    private val grass2 = grass(grass1.rightmostX, groundY, SCROLL_SPEED)

    private val pipe1 = pipe(210.0, 0.0, 60, SCROLL_SPEED, groundY)
    private val pipe2 = pipe(pipe1.rightmostX + PIPE_GAP, 0.0, 70, SCROLL_SPEED, groundY)
    private val pipe3 = pipe(pipe2.rightmostX + PIPE_GAP, 0.0, 60, SCROLL_SPEED, groundY)

    fun update(delta: HRTimeSpan) {
        grass1.update(delta)
        grass2.update(delta)
        pipe1.update(delta)
        pipe2.update(delta)
        pipe3.update(delta)

        when {
            pipe1.isScrolledLeft -> pipe1.reset(pipe3.rightmostX + PIPE_GAP)
            pipe2.isScrolledLeft -> pipe2.reset(pipe1.rightmostX + PIPE_GAP)
            pipe3.isScrolledLeft -> pipe3.reset(pipe2.rightmostX + PIPE_GAP)
        }

        when {
            grass1.isScrolledLeft -> grass1.reset(grass2.rightmostX)
            grass2.isScrolledLeft -> grass2.reset(grass1.rightmostX)
        }
    }

    fun stop() {
        grass1.stop()
        grass2.stop()
        pipe1.stop()
        pipe2.stop()
        pipe3.stop()
    }

    fun needsScore(bird: Bird): Boolean {
        fun shouldScore(pipe: Pipe): Boolean {
            return !pipe.isScored && pipe.centerX < bird.rightmostX
        }

        fun score(pipe: Pipe) {
            pipe.isScored = true
            AssetLoader.coin.play(coinPlaybackParameters)
        }

        return when {
            shouldScore(pipe1) -> true.also { score(pipe1) }
            shouldScore(pipe2) -> true.also { score(pipe2) }
            shouldScore(pipe3) -> true.also { score(pipe3) }
            else -> false
        }
    }

    fun collides(bird: Bird): Boolean {
        return pipe1.collides(bird) || pipe2.collides(bird) || pipe3.collides(bird)
    }

    fun onRestart() {
        grass1.onRestart(0.0, SCROLL_SPEED)
        grass2.onRestart(grass1.rightmostX, SCROLL_SPEED)
        pipe1.onRestart(210.0, SCROLL_SPEED)
        pipe2.onRestart(pipe1.rightmostX + PIPE_GAP, SCROLL_SPEED)
        pipe3.onRestart(pipe2.rightmostX + PIPE_GAP, SCROLL_SPEED)
    }

    companion object {

        const val SCROLL_SPEED = -59.0
        const val PIPE_GAP = 49

        private val coinPlaybackParameters = PlaybackParameters.DEFAULT.copy(volume = 0.1)
    }
}