package com.mvvm.example.utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun Int.scaledSp(): TextUnit {
    val value: Float = this.toFloat() //2.625
    return with(LocalDensity.current) {
        val fontScale = this.fontScale
        val textSize =  (value) / fontScale
        textSize.sp
    }
}


val Int.scaledSp: TextUnit
    @Composable get() =  scaledSp()