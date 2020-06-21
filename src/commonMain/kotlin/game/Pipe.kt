package game

import com.soywiz.kds.iterators.fastForEach
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import helper.AssetLoader
import kotlin.random.Random

fun Container.pipe(x: Double, y: Double, upperBarHeight: Int, scrollSpeed: Double, groundY: Double) =
        Pipe(x, y, upperBarHeight, scrollSpeed, groundY).addTo(this)

class Pipe(x: Double, y: Double, private var upperBarHeight: Int, scrollSpeed: Double, private val groundY: Double)
    : HScrollable(x, y, barWidth, scrollSpeed) {

    private val barUpImg = scrollingContainer.image(AssetLoader.bar) { smoothing = false }
    private val barDownImg = scrollingContainer.image(AssetLoader.bar) { smoothing = false }
    private val skullUpImg = scrollingContainer.image(AssetLoader.skullUp) { smoothing = false }
    private val skullDownImg = scrollingContainer.image(AssetLoader.skullDown) { smoothing = false }

    private val barUp = scrollingContainer.solidRect(0, 0, RGBA(255, 0, 0, 127))
    private val barDown = scrollingContainer.solidRect(0, 0, RGBA(255, 0, 0, 127))
    private val skullUp = scrollingContainer.solidRect(skullWidth, skullHeight, RGBA(255, 0, 0, 127))
    private val skullDown = scrollingContainer.solidRect(skullWidth, skullHeight, RGBA(255, 0, 0, 127))

    private val collisionSet = listOf(skullUp, skullDown, barUp, barDown)

    private val leftmostX get() = position.x - (skullWidth - barWidth) / 2

    init {
        onBarHeightChange()
    }

    override fun onReset() {
        upperBarHeight = Random.nextInt(15, 105)
        onBarHeightChange()
    }

    private fun onBarHeightChange() {
        updateBars()
        updateSkulls()
        updateBarsSize()
    }

    private fun updateBars() {
        barDownImg.xy(0, upperBarHeight + VERTICAL_GAP)
        barDown.xy(0, upperBarHeight + VERTICAL_GAP)
    }

    private fun updateSkulls() {
        skullUpImg.xy(-(skullWidth - barWidth) / 2, upperBarHeight - skullHeight)
        skullDownImg.xy(-(skullWidth - barWidth) / 2, upperBarHeight + VERTICAL_GAP)

        skullUp.xy(-(skullWidth - barWidth) / 2, upperBarHeight - skullHeight)
        skullDown.xy(-(skullWidth - barWidth) / 2, upperBarHeight + VERTICAL_GAP)
    }

    private fun updateBarsSize() {
        barUpImg.size(barWidth, upperBarHeight)
        barDownImg.size(barWidth.toDouble(), groundY - (position.y + upperBarHeight + VERTICAL_GAP))

        barUp.size(barWidth, upperBarHeight)
        barDown.size(barWidth.toDouble(), groundY - (position.y + upperBarHeight + VERTICAL_GAP))
    }

    fun collides(bird: Bird): Boolean {
        fun colorRect(r: SolidRect) {
            val color = if (bird.boundingCircle.collidesWith(r, CollisionKind.SHAPE)) {
                Colors.YELLOW
            } else {
                RGBA(255, 0, 0, 127)
            }

            r.color = color
        }

        collisionSet.fastForEach(::colorRect)

//        if (leftmostX < bird.x + bird.boundingCircleRadius) {
////            return bird.boundingCircle.collidesWith(skullUp, CollisionKind.SHAPE) ||
////                    bird.boundingCircle.collidesWith(skullDown, CollisionKind.SHAPE) ||
////                    bird.boundingCircle.collidesWith(barUp, CollisionKind.SHAPE) ||
////                    bird.boundingCircle.collidesWith(barDown, CollisionKind.SHAPE)
//        }

        return false
    }

    companion object {

        private const val VERTICAL_GAP = 45

        private val skullWidth by lazy { AssetLoader.skullDown.width }
        private val skullHeight by lazy { AssetLoader.skullDown.height }

        private val barWidth by lazy { AssetLoader.bar.width }
    }
}
