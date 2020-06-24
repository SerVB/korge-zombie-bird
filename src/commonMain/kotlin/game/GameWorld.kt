package game

import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.korge.view.*
import helper.AssetLoader

fun Container.gameWorld() = GameWorld().addTo(this)

class GameWorld : Container() {

    var score = 0

    private val backgroundView: BackgroundView
    private val scroller: ScrollHandler
    private val bird: Bird
    private val scoreShadow: Text
    private val scoreText: Text
    private val groundY: Double

    init {
        val midPointY = gameVirtualHeight / 2.0
        groundY = midPointY + 66

        backgroundView = backgroundView(midPointY)
        scroller = scrollHandler(groundY)
        bird = bird(33.0 + 17 / 2, midPointY - 5 + 12 / 2)
        scoreShadow = text("", textSize = 18.0, font = AssetLoader.shadow)
        scoreText = text("", textSize = 18.0, font = AssetLoader.text)

        updateScoreViews()
    }

    fun update(delta: HRTimeSpan) {
        bird.update(delta)
        scroller.update(delta)

        if (scroller.needsScore(bird)) {
            ++score
            updateScoreViews()
        }

        if (bird.isAlive && scroller.collides(bird)) {
            scroller.stop()
            AssetLoader.dead.play()
            bird.die()
        }

        if (bird.lowestY > groundY) {
            scroller.stop()
            bird.die()
            bird.decelerate()
        }
    }

    fun onClick() {
        bird.onClick()
    }

    private fun updateScoreViews() {
        val scoreString = score.toString()
        val midPointX = gameVirtualWidth / 2
        val shadowX = midPointX - 3 * scoreString.length
        val shadowY = 12

        scoreShadow.apply { text = scoreString }.xy(shadowX, shadowY)
        scoreText.apply { text = scoreString }.xy(shadowX + 1, shadowY - 1)
    }

    companion object {

        const val gameVirtualWidth = 136
        const val gameVirtualHeight = 204
    }
}