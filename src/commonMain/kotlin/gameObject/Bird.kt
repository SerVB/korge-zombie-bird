package gameObject

import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.korma.geom.Point

class Bird(x: Double, y: Double, val width: Int, val height: Int) {

    var rotationDegrees = 0.0
        private set

    private val position = Point(x, y)  // todo: is there a vector2d class just to name it more suitable?
    private val velocity = Point()
    private val acceleration = Point(0, 460)  // todo: how to make it immutable?

    val boundingCircleRadius = 6.5
    val boundingCirclePoint = Point()

    fun update(delta: HRTimeSpan) {
        velocity += acceleration * delta.secondsDouble

        if (velocity.y > 200) {
            velocity.y = 200.0
        }

        position += velocity * delta.secondsDouble
        boundingCirclePoint.setTo(position.x - boundingCircleRadius, position.y - boundingCircleRadius)

        if (velocity.y < 0) {
            rotationDegrees -= 600 * delta.secondsDouble

            if (rotationDegrees < -20) {
                rotationDegrees = -20.0
            }
        }

        if (isFalling) {
            rotationDegrees += 480 * delta.secondsDouble

            if (rotationDegrees > 90) {
                rotationDegrees = 90.0
            }
        }
    }

    fun onClick() {
        velocity.y = -140.0
    }

    val x get() = position.x
    val y get() = position.y

    val isFalling get() = velocity.y > 110
    val isFlapping get() = velocity.y <= 70
}
