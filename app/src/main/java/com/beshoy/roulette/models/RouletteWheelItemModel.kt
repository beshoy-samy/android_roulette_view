package com.beshoy.roulette.models

import androidx.annotation.ColorRes
import com.beshoy.roulette.views.RouletteWheel
import kotlin.math.cos
import kotlin.math.sin

data class RouletteWheelItemModel(
    @ColorRes val backgroundColor: Int,
    val text: String? = null
) {

    internal var startingAngle = 0f
    internal var endingAngle = 0f
    internal var sweepAngle = 0f
    internal lateinit var startingPointCoordinates: Pair<Float, Float>

    internal fun updateItemProperties(index: Int, sweepAngle: Float) {
        this.sweepAngle = sweepAngle
        startingAngle = sweepAngle.times(index)
        endingAngle = startingAngle.plus(sweepAngle)
    }

    internal fun calculateStartingPointCoordinates(viewRadius: Float, wheelRadius: Float): Pair<Float, Float> {
        val radian = getRadian()
        val xPoint = cos(radian).times(wheelRadius).plus(viewRadius)
        val yPoint = sin(radian).times(wheelRadius).plus(viewRadius)
        startingPointCoordinates = Pair(xPoint, yPoint)
        return startingPointCoordinates
    }

    internal fun getRadian() = Math.toRadians(startingAngle.toDouble()).toFloat()

    internal fun updateItemPropertiesAfterRotation(rotation: Float) {
        startingAngle = startingAngle.plus(rotation).rem(RouletteWheel.CIRCLE_SIZE)
        val calculatedEndingAngle = startingAngle.plus(sweepAngle).rem(RouletteWheel.CIRCLE_SIZE)
        endingAngle =
            if (calculatedEndingAngle == 0f) RouletteWheel.CIRCLE_SIZE else calculatedEndingAngle
    }

    internal fun reset(index: Int) = updateItemProperties(index, sweepAngle)
}