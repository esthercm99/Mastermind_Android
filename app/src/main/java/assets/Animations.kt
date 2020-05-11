package assets

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

fun View.blink(times: Int = Animation.INFINITE, duration: Long = 1000L, offset: Long = 20L, minAlpha: Float = 0.1f, maxAlpha: Float = 1.0f, repeatMode: Int = Animation.REVERSE ) {
    startAnimation(AlphaAnimation(minAlpha, maxAlpha).also {
        it.duration = duration
        it.startOffset = offset
        it.repeatMode = repeatMode
        it.repeatCount = times
    })
}
