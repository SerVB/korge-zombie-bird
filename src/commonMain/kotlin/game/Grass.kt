package game

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import helper.AssetLoader

fun Container.grass(x: Double, y: Double, scrollSpeed: Double) = Grass(x, y, scrollSpeed).addTo(this)

class Grass(x: Double, y: Double, scrollSpeed: Double) : HScrollable(x, y, AssetLoader.grass.width, scrollSpeed) {

    init {
        scrollingContainer.image(AssetLoader.grass) { smoothing = false }
    }
}
