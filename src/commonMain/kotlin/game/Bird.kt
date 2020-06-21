package game

import com.soywiz.klock.DateTime
import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.kmem.toIntFloor
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.BmpSlice
import com.soywiz.korim.color.RGBA
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.Point
import helper.AssetLoader

fun Container.bird(x: Double, y: Double) = Bird(x = x, y = y).addTo(this)

class Bird(x: Double, y: Double) : Container() {

    private var birdRotationDegrees = 0.0

    private val position = Point(x, y)  // todo: is there a vector2d class just to name it more suitable?
    private val velocity = Point()
    private val acceleration = Point(0, 460)  // todo: how to make it immutable?

    private val image: Image
    val boundingCircle: Circle

    init {
        image = image(currentBirdImage, 0.5, 0.5) { smoothing = false }
        boundingCircle = circle(BOUNDING_RADIUS, RGBA(255, 0, 0, 127)).xy(-BOUNDING_RADIUS, -BOUNDING_RADIUS)
    }

    fun update(delta: HRTimeSpan) {
        velocity += acceleration * delta.secondsDouble

        if (velocity.y > 200) {
            velocity.y = 200.0
        }

        position += velocity * delta.secondsDouble

        if (velocity.y < 0) {
            birdRotationDegrees -= 600 * delta.secondsDouble

            if (birdRotationDegrees < -20) {
                birdRotationDegrees = -20.0
            }
        }

        if (isFalling) {
            birdRotationDegrees += 480 * delta.secondsDouble

            if (birdRotationDegrees > 90) {
                birdRotationDegrees = 90.0
            }
        }

        this.xy(position.x, position.y)
        image.rotation(Angle.fromDegrees(birdRotationDegrees))
    }

    fun onClick() {
        velocity.y = -140.0
    }

    private val isFalling get() = velocity.y > 110
    private val isFlapping get() = velocity.y <= 70

    private val currentBirdImage: BmpSlice
        get() = when (isFlapping) {
            true -> currentBirdFrame
            false -> AssetLoader.birdMid
        }

    companion object {

        private val birdFrames by lazy { with(AssetLoader) { listOf(birdDown, birdMid, birdUp, birdMid) } }
        private const val millisPerBirdFrame = 60

        private val currentBirdFrame: BmpSlice
            get() {
                // todo: is there a way to select the frame automatically?
                val frameId = ((DateTime.nowUnix() / millisPerBirdFrame) % birdFrames.size).toIntFloor()
                return birdFrames[frameId]
            }

        private const val BOUNDING_RADIUS = 6.5
    }
}
