package gameObject

import com.soywiz.korge.view.Circle
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import kotlin.random.Random

class Pipe(x: Double, y: Double, width: Int, height: Int, scrollSpeed: Double, private val groundY: Double)
    : Scrollable(x = x, y = y, width = width, height = height, scrollSpeed = scrollSpeed) {

    val skullUp = SolidRect(SKULL_WIDTH, SKULL_HEIGHT, Colors.TRANSPARENT_WHITE)
    val skullDown = SolidRect(SKULL_WIDTH, SKULL_HEIGHT, Colors.TRANSPARENT_WHITE)
    val barUp = SolidRect(width, height, Colors.TRANSPARENT_WHITE)
    val barDown = SolidRect(0, 0, Colors.TRANSPARENT_WHITE)

    private val collisionSet = listOf(skullUp, skullDown, barUp, barDown)

    private val leftmostX get() = position.x - (SKULL_WIDTH - width) / 2

    override fun onUpdate() {
        skullUp.xy(position.x - (SKULL_WIDTH - width) / 2, position.y + height - SKULL_HEIGHT)
        skullDown.xy(position.x - (SKULL_WIDTH - width) / 2, barDown.y)

        barUp.xy(position.x, position.y)
                .setSize(width.toDouble(), height.toDouble())
        barDown.xy(position.x, position.y + height + VERTICAL_GAP)
                .setSize(width.toDouble(), groundY - (position.y + height + VERTICAL_GAP))
    }

    override fun onReset() {
        height = Random.nextInt(15, 105)
    }

    fun collides(bird: Bird): Boolean {
        if (leftmostX < bird.x + bird.boundingCircleRadius) {
            // todo: it's strange that such conversions are needed
            val birdBoundingCircle = Circle(bird.boundingCircleRadius).xy(bird.boundingCirclePoint.x, bird.boundingCirclePoint.y)

//            return birdBoundingCircle.collidesWith(skullUp, CollisionKind.SHAPE) ||
//                    birdBoundingCircle.collidesWith(skullDown, CollisionKind.SHAPE) ||
//                    birdBoundingCircle.collidesWith(barUp, CollisionKind.SHAPE) ||
//                    birdBoundingCircle.collidesWith(barDown, CollisionKind.SHAPE)
        }

        return false
    }

    companion object {

        const val VERTICAL_GAP = 45
        const val SKULL_WIDTH = 24
        const val SKULL_HEIGHT = 11
    }
}
