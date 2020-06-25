package helper

import com.soywiz.korge.service.storage.NativeStorage
import com.soywiz.korge.service.storage.contains
import com.soywiz.korge.service.storage.get

object Storage {

    private const val HIGH_SCORE = "highScore"

    fun init() {
        if (HIGH_SCORE !in NativeStorage) {
            NativeStorage[HIGH_SCORE] = "0"
        }
    }

    var highScore: Int
        get() = NativeStorage[HIGH_SCORE].toInt()
        set(value) {
            NativeStorage[HIGH_SCORE] = value.toString()
        }
}