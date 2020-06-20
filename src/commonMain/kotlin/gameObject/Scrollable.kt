package gameObject

import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.korma.geom.Point

abstract class Scrollable(x: Double, y: Double, width: Int, height: Int, scrollSpeed: Double) {

    var width: Int = width
        protected set

    var height: Int = height
        protected set

    protected val position = Point(x, y)
    protected val velocity = Point(scrollSpeed, 0.0)

    var isScrolledLeft = false
        protected set

    fun update(delta: HRTimeSpan) {
        position += velocity * delta.secondsDouble

        if (position.x + width < 0) {
            isScrolledLeft = true
        }

        onUpdate()
    }

    protected open fun onUpdate() {}

    fun stop() {
        velocity.x = 0.0
    }

    fun reset(newX: Double) {
        position.x = newX
        isScrolledLeft = false

        onReset()
    }

    protected open fun onReset() {}

    val tailX get() = position.x + width
    val x get() = position.x
    val y get() = position.y
}