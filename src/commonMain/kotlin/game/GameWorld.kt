package game

import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.korge.view.*
import helper.AssetLoader
import helper.Storage

fun Container.gameWorld() = GameWorld().addTo(this)

private enum class GameState {
    READY, RUNNING, GAME_OVER, HIGH_SCORE,
}

class GameWorld : Container() {

    var score = 0
    val isReady get() = currentState == GameState.READY
    val isGameOver get() = currentState == GameState.GAME_OVER
    val isHighScore get() = currentState == GameState.HIGH_SCORE

    private var currentState = GameState.READY

    private val backgroundView: BackgroundView
    private val scroller: ScrollHandler
    private val bird: Bird
    private val scoreShadow: Text  // todo: make own view for text with shadow
    private val scoreText: Text
    private val touchMeShadow: Text
    private val touchMeText: Text
    private val gameOverShadow: Text
    private val gameOverText: Text
    private val highScoreShadow: Text
    private val highScoreText: Text
    private val highScoreValuePrefixShadow: Text
    private val highScoreValuePrefixText: Text
    private val highScoreValueShadow: Text
    private val highScoreValueText: Text
    private val tryAgainShadow: Text
    private val tryAgainText: Text
    private val groundY: Double

    init {
        val midPointY = gameVirtualHeight / 2.0
        groundY = midPointY + 66

        backgroundView = backgroundView(midPointY)
        scroller = scrollHandler(groundY)
        bird = bird(33.0 + 17 / 2, midPointY - 5 + 12 / 2)
        scoreShadow = text("", textSize = 18.0, font = AssetLoader.shadow)
        scoreText = text("", textSize = 18.0, font = AssetLoader.text)
        touchMeShadow = text("Touch me", textSize = 18.0, font = AssetLoader.shadow).xy((136 / 2) - (42), 76)
        touchMeText = text("Touch me", textSize = 18.0, font = AssetLoader.text).xy((136 / 2) - (42 - 1), 75)
        gameOverShadow = text("Game Over", textSize = 18.0, font = AssetLoader.shadow).xy(25, 56)
        gameOverText = text("Game Over", textSize = 18.0, font = AssetLoader.text).xy(24, 55)  // todo: don't forget there are multiple directions of shadow
        highScoreShadow = text("High Score!", textSize = 18.0, font = AssetLoader.shadow).xy(19, 56)
        highScoreText = text("High Score!", textSize = 18.0, font = AssetLoader.text).xy(18, 55)
        highScoreValuePrefixShadow = text("High Score:", textSize = 18.0, font = AssetLoader.shadow).xy(23, 106)
        highScoreValuePrefixText = text("High Score:", textSize = 18.0, font = AssetLoader.text).xy(22, 105)
        highScoreValueShadow = text("", textSize = 18.0, font = AssetLoader.shadow)
        highScoreValueText = text("", textSize = 18.0, font = AssetLoader.text)
        tryAgainShadow = text("Try again?", textSize = 18.0, font = AssetLoader.shadow).xy(23, 76)
        tryAgainText = text("Try again?", textSize = 18.0, font = AssetLoader.text).xy(24, 75)

        updateScoreViews()
    }

    fun start() {
        currentState = GameState.RUNNING
    }

    fun restart() {
        val midPointY = gameVirtualHeight / 2.0
        currentState = GameState.READY
        score = 0
        scroller.onRestart()
        bird.onRestart(midPointY - 5 + 12 / 2)
        updateScoreViews()
    }

    fun update(delta: HRTimeSpan) {
        when (currentState) {
            GameState.READY -> {
                touchMeShadow.visible = true
                touchMeText.visible = true
                gameOverShadow.visible = false
                gameOverText.visible = false
                highScoreShadow.visible = false
                highScoreText.visible = false
                highScoreValuePrefixShadow.visible = false
                highScoreValuePrefixText.visible = false
                highScoreValueShadow.visible = false
                highScoreValueText.visible = false
                tryAgainShadow.visible = false
                tryAgainText.visible = false
            }
            GameState.RUNNING -> {
                gameOverShadow.visible = false
                gameOverText.visible = false
                highScoreShadow.visible = false
                highScoreText.visible = false
                highScoreValuePrefixShadow.visible = false
                highScoreValuePrefixText.visible = false
                highScoreValueShadow.visible = false
                highScoreValueText.visible = false
                tryAgainShadow.visible = false
                tryAgainText.visible = false

                updateRunning(delta)
            }
            GameState.GAME_OVER -> {
                gameOverShadow.visible = true
                gameOverText.visible = true
                highScoreShadow.visible = false
                highScoreText.visible = false
                highScoreValuePrefixShadow.visible = true
                highScoreValuePrefixText.visible = true
                highScoreValueShadow.visible = true
                highScoreValueText.visible = true
                tryAgainShadow.visible = true
                tryAgainText.visible = true

                val scoreString = Storage.highScore.toString()
                val midPointX = gameVirtualWidth / 2
                val shadowX = midPointX - 3 * scoreString.length
                val shadowY = 128
                highScoreValueShadow.apply { text = scoreString }.xy(shadowX, shadowY)
                highScoreValueText.apply { text = scoreString }.xy(shadowX + 1, shadowY - 1)

                updateRunning(delta)
            }
            GameState.HIGH_SCORE -> {
                gameOverShadow.visible = false
                gameOverText.visible = false
                highScoreShadow.visible = true
                highScoreText.visible = true
                highScoreValuePrefixShadow.visible = false
                highScoreValuePrefixText.visible = false
                highScoreValueShadow.visible = false
                highScoreValueText.visible = false
                tryAgainShadow.visible = true
                tryAgainText.visible = true

                updateRunning(delta)
            }
        }
    }

    private fun updateRunning(delta: HRTimeSpan) {
        touchMeShadow.visible = false
        touchMeText.visible = false

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

        if (currentState == GameState.RUNNING && bird.lowestY > groundY) {
            scroller.stop()
            bird.die()
            bird.decelerate()

            if (score > Storage.highScore) {
                Storage.highScore = score
                currentState = GameState.HIGH_SCORE
            } else {
                currentState = GameState.GAME_OVER
            }
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