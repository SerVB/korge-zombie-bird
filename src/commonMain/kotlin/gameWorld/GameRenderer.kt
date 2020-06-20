package gameWorld

import com.soywiz.klock.DateTime
import com.soywiz.kmem.toIntFloor
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.BmpSlice
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korma.geom.Angle
import gameObject.Grass
import gameObject.Pipe
import helper.AssetLoader

class GameRenderer(private val world: GameWorld) {

    fun render(stage: Stage) {
        val midPointY = GameWorld.gameVirtualHeight / 2

        stage.apply {
            removeChildren()  // todo: reuse children

            solidRect(136, midPointY + 66, RGBA(55, 80, 100, 255)).xy(0, 0)  // background
            solidRect(136, 11, RGBA(111, 186, 45, 255)).xy(0, midPointY + 66)  // grass
            solidRect(136, 52, RGBA(147, 80, 27, 255)).xy(0, midPointY + 77)  // dirt

            image(AssetLoader.bg) { smoothing = false }.xy(0, midPointY + 23)  // todo: make bg move but slower than foreground

            drawGrass(this)
            drawPipes(this)
            drawSkulls(this)

            val birdFrame = when (world.bird.isFlapping) {
                true -> currentBirdFrame
                false -> AssetLoader.birdMid
            }

            image(birdFrame, 0.5, 0.5)
                    .apply { smoothing = false }
                    .rotation(Angle.fromDegrees(world.bird.rotationDegrees))
                    .xy(world.bird.x, world.bird.y)

            val birdBoundingCircle = circle(world.bird.boundingCircleRadius, RGBA(255, 0, 0, 127))
                    .xy(world.bird.boundingCirclePoint.x, world.bird.boundingCirclePoint.y)

            fun paintRect(rectangle: SolidRect) {
                val r = solidRect(rectangle.width, rectangle.height, Colors.BLACK)
                        .xy(rectangle.x, rectangle.y)

                val color = if (birdBoundingCircle.collidesWith(r, CollisionKind.SHAPE)) {
                    Colors.YELLOW
                } else {
                    RGBA(255, 0, 0, 127)
                }

                r.color = color
            }

            paintRect(world.scroller.pipe1.barDown)
            paintRect(world.scroller.pipe2.barDown)
            paintRect(world.scroller.pipe3.barDown)

            paintRect(world.scroller.pipe1.barUp)
            paintRect(world.scroller.pipe2.barUp)
            paintRect(world.scroller.pipe3.barUp)

            paintRect(world.scroller.pipe1.skullDown)
            paintRect(world.scroller.pipe2.skullDown)
            paintRect(world.scroller.pipe3.skullDown)

            paintRect(world.scroller.pipe1.skullUp)
            paintRect(world.scroller.pipe2.skullUp)
            paintRect(world.scroller.pipe3.skullUp)
        }
    }

    private fun drawGrass(stage: Stage) {
        stage.apply {
            fun drawItem(grass: Grass) {
                image(AssetLoader.grass) { smoothing = false }.xy(grass.x, grass.y)
            }

            drawItem(world.scroller.grass1)
            drawItem(world.scroller.grass2)
        }
    }

    private fun drawSkulls(stage: Stage) {
        stage.apply {
            fun drawItem(pipe: Pipe) {
                image(AssetLoader.skullUp)
                        .apply { smoothing = false }
                        .xy(pipe.x - 1, pipe.y + pipe.height - 14)
                image(AssetLoader.skullDown)
                        .apply { smoothing = false }
                        .xy(pipe.x - 1, pipe.y + pipe.height + 45)
            }

            drawItem(world.scroller.pipe1)
            drawItem(world.scroller.pipe2)
            drawItem(world.scroller.pipe3)
        }
    }

    private fun drawPipes(stage: Stage) {
        val midPointY = GameWorld.gameVirtualHeight / 2

        stage.apply {
            fun drawItem(pipe: Pipe) {
                image(AssetLoader.bar)
                        .apply { smoothing = false }
                        .xy(pipe.x, pipe.y)
                        .size(pipe.width, pipe.height)
                image(AssetLoader.bar)
                        .apply { smoothing = false }
                        .xy(pipe.x, pipe.y + pipe.height + 45)
                        .size(pipe.width, midPointY + 66 - (pipe.height + 45))
            }

            drawItem(world.scroller.pipe1)
            drawItem(world.scroller.pipe2)
            drawItem(world.scroller.pipe3)
        }
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
    }
}