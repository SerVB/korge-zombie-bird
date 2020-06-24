package helper

import com.soywiz.korau.sound.NativeSound
import com.soywiz.korau.sound.readSound
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BmpSlice
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.RectangleInt

object AssetLoader {

    lateinit var texture: Bitmap private set

    lateinit var bg: BmpSlice private set
    lateinit var grass: BmpSlice private set

    lateinit var birdDown: BmpSlice private set
    lateinit var birdMid: BmpSlice private set
    lateinit var birdUp: BmpSlice private set

    lateinit var skullUp: BmpSlice private set
    lateinit var skullDown: BmpSlice private set
    lateinit var bar: BmpSlice private set

    lateinit var dead: NativeSound private set

    suspend fun load() {
        texture = resourcesVfs["texture.png"].readBitmap()

        bg = texture.slice(RectangleInt(0, 0, 136, 43))
        grass = texture.slice(RectangleInt(0, 43, 143, 11))

        birdDown = texture.slice(RectangleInt(136, 0, 17, 12))
        birdMid = texture.slice(RectangleInt(153, 0, 17, 12))
        birdUp = texture.slice(RectangleInt(170, 0, 17, 12))

        skullUp = texture.slice(RectangleInt(192, 0, 24, 14)).extract().flipY().slice()  // todo: can a slice just be flipped?
        skullDown = texture.slice(RectangleInt(192, 0, 24, 14))
        bar = texture.slice(RectangleInt(136, 16, 22, 3))

        dead = resourcesVfs["dead.wav"].readSound()
    }
}