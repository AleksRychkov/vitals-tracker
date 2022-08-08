package ru.vt.core.sysservice.dependency

import android.content.Context
import ru.vt.core.sysservice.VibratorService
import ru.vt.core.sysservice.VibratorServiceImpl

fun provideVibratorService(context: Context): VibratorService = VibratorServiceImpl(context)