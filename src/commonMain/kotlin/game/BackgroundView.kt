package game

import com.soywiz.korge.view.*
import com.soywiz.korim.color.RGBA
import helper.AssetLoader

fun Container.backgroundView(midPointY: Double) = BackgroundView(midPointY).addTo(this)

class BackgroundView(midPointY: Double) : Container() {

    init {
        solidRect(136.0, midPointY + 66, RGBA(55, 80, 100, 255)).xy(0, 0)  // background
        solidRect(136, 11, RGBA(111, 186, 45, 255)).xy(0.0, midPointY + 66)  // grass
        solidRect(136, 52, RGBA(147, 80, 27, 255)).xy(0.0, midPointY + 77)  // dirt
        image(AssetLoader.bg) { smoothing = false }.xy(0.0, midPointY + 23)  // todo: make bg move but slower than foreground
    }
}