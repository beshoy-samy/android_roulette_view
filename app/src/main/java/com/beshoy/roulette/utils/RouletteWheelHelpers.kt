package com.beshoy.roulette.utils

import android.view.View
import timber.log.Timber
import kotlin.math.roundToInt

/**
 * @return roulette view size
 */
internal fun onMeasureRouletteViewSize(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int,
    minimumSize: Float,
    strokeWidth: Float,
    padding: Int
): Int {
    Timber.i("onMeasureRouletteViewSize width ${View.MeasureSpec.toString(widthMeasureSpec)}")
    Timber.i("onMeasureRouletteViewSize height ${View.MeasureSpec.toString(heightMeasureSpec)}")

    val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
    val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
    val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
    val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

    var rouletteViewSize = minimumSize.toInt()

    if (widthMode == View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY)
        rouletteViewSize = minOf(widthSize, heightSize)
    else if (widthMode == View.MeasureSpec.EXACTLY)
        rouletteViewSize = widthSize
    else if (heightMode == View.MeasureSpec.EXACTLY)
        rouletteViewSize = heightSize

    return rouletteViewSize.plus(strokeWidth).plus(padding).roundToInt()
}
