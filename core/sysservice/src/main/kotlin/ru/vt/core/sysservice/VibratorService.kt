package ru.vt.core.sysservice

import android.content.Context
import android.os.Vibrator

interface VibratorService {
    fun performTick()
}

@Suppress("DEPRECATION")
internal class VibratorServiceImpl(private val context: Context) : VibratorService {
    private companion object {
        const val TICK_DURATION: Long = 1L
    }

    private val vibrator: Vibrator? by lazy {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }

    override fun performTick() {
        wrapper {
            vibrate(TICK_DURATION)
        }
    }

    private fun wrapper(block: Vibrator.() -> Unit) {
        vibrator?.run {
            if (hasVibrator()) {
                block(this)
            }
        }
    }
}