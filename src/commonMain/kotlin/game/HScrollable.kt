package game

import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.container
import com.soywiz.korge.view.xy
import com.soywiz.korma.geom.Point

// todo: rename to LeftScrollable
abstract class HScrollable(
        x: Double, y: Double, width: Int,
        scrollSpeed: Double
) : Container() {

    protected val scrollingContainer = container()

    private val myWidth = width

    protected val position = Point(x, y)
    private val velocity = Point(scrollSpeed, 0.0)

    val isScrolledLeft get() = rightmostX < 0

    fun update(delta: HRTimeSpan) {
        position += velocity * delta.secondsDouble
        scrollingContainer.xy(position.x, position.y)
        onUpdate()
    }

    protected open fun onUpdate() {}

    fun stop() {
        velocity.x = 0.0
    }

    fun reset(newX: Double) {
        position.x = newX
        onReset()
    }

    protected open fun onReset() {}

    val rightmostX get() = position.x + myWidth
}