package game

import com.soywiz.klock.DateTime
import com.soywiz.klock.hr.HRTimeSpan
import com.soywiz.kmem.toIntFloor
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.BmpSlice
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.Point
import helper.AssetLoader

fun Container.bird(x: Double, y: Double) = Bird(x = x, y = y).addTo(this)

class Bird(x: Double, y: Double) : Container() {

    var isAlive = true
        private set

    private var birdRotationDegrees = 0.0

    private val position = Point(x, y)  // todo: is there a vector2d class just to name it more suitable?
    private val velocity = Point()
    private val acceleration = Point(0, 460)  // todo: how to make it immutable? (don't need it here but for the future)

    private val image: Image
    val boundingCircle: Circle

    val lowestY get() = position.y + BOUNDING_RADIUS
    val rightmostX get() = position.x + BOUNDING_RADIUS

    init {
        image = image(currentBirdImage, 0.5, 0.5) { smoothing = false }
        boundingCircle = circle(BOUNDING_RADIUS, Colors.TRANSPARENT_WHITE).xy(-BOUNDING_RADIUS, -BOUNDING_RADIUS)
    }

    fun update(delta: HRTimeSpan) {
        velocity += acceleration * delta.secondsDouble
        velocity.y = minOf(200.0, velocity.y)

        position += velocity * delta.secondsDouble

        if (velocity.y < 0) {
            birdRotationDegrees -= 600 * delta.secondsDouble
            birdRotationDegrees = maxOf(-20.0, birdRotationDegrees)
        }

        if (isFalling || !isAlive) {
            birdRotationDegrees += 480 * delta.secondsDouble
            birdRotationDegrees = minOf(90.0, birdRotationDegrees)
        }

        this.xy(position.x, position.y)
        image
                .rotation(Angle.fromDegrees(birdRotationDegrees))
                .apply { texture = currentBirdImage }
    }

    fun onClick() {
        if (isAlive) {
            velocity.y = -140.0
            AssetLoader.flap.play()
        }
    }

    fun die() {
        isAlive = false
        velocity.y = 0.0
    }

    fun decelerate() {
        acceleration.y = 0.0
    }

    private val isFalling get() = velocity.y > 110
    private val isFlapping get() = isAlive && velocity.y <= 70

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
