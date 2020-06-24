package game

import com.soywiz.korge.view.*
import helper.AssetLoader
import kotlin.random.Random

fun Container.pipe(x: Double, y: Double, upperBarHeight: Int, scrollSpeed: Double, groundY: Double) =
        Pipe(x, y, upperBarHeight, scrollSpeed, groundY).addTo(this)

class Pipe(x: Double, y: Double, private var upperBarHeight: Int, scrollSpeed: Double, private val groundY: Double)
    : HScrollable(x, y, barWidth, scrollSpeed) {

    var isScored = false

    private val barUpImg = scrollingContainer.image(AssetLoader.bar) { smoothing = false }
    private val barDownImg = scrollingContainer.image(AssetLoader.bar) { smoothing = false }
    private val skullUpImg = scrollingContainer.image(AssetLoader.skullUp) { smoothing = false }
    private val skullDownImg = scrollingContainer.image(AssetLoader.skullDown) { smoothing = false }

    private val collisionSet = listOf(skullUpImg, skullDownImg, barUpImg, barDownImg)

    private val leftmostX get() = position.x - (skullWidth - barWidth) / 2
    val centerX get() = position.x + barWidth / 2

    init {
        onBarHeightChange()
    }

    override fun onReset() {
        upperBarHeight = Random.nextInt(15, 105)
        onBarHeightChange()
        isScored = false
    }

    private fun onBarHeightChange() {
        updateBars()
        updateSkulls()
        updateBarsSize()
    }

    private fun updateBars() {
        barDownImg.xy(0, upperBarHeight + VERTICAL_GAP)
    }

    private fun updateSkulls() {
        skullUpImg.xy(-(skullWidth - barWidth) / 2, upperBarHeight - skullHeight)
        skullDownImg.xy(-(skullWidth - barWidth) / 2, upperBarHeight + VERTICAL_GAP)
    }

    private fun updateBarsSize() {
        barUpImg.size(barWidth, upperBarHeight)
        barDownImg.size(barWidth.toDouble(), groundY - (position.y + upperBarHeight + VERTICAL_GAP))
    }

    fun collides(bird: Bird): Boolean {
        if (leftmostX < bird.x + bird.boundingCircle.radius) {
            // todo: use fastAny function
            return collisionSet.any { bird.boundingCircle.collidesWith(it, CollisionKind.SHAPE) }
        }

        return false
    }

    companion object {

        private const val VERTICAL_GAP = 45

        private val skullWidth get() = AssetLoader.skullDown.width
        private val skullHeight get() = AssetLoader.skullDown.height

        private val barWidth get() = AssetLoader.bar.width
    }
}
