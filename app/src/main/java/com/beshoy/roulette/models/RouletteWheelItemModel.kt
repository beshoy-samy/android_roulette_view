package com.beshoy.roulette.models

import androidx.annotation.ColorRes
import com.beshoy.roulette.views.RouletteWheel

data class RouletteWheelItemModel(
    @ColorRes val backgroundColor: Int,
    val text: String? = null
) {

    internal var startingAngle = 0f
    internal var endingAngle = 0f
    internal var sweepAngle = 0f

    internal fun updateItemProperties(index: Int, sweepAngle: Float) {
        this.sweepAngle = sweepAngle
        startingAngle = sweepAngle.times(index)
        endingAngle = startingAngle.plus(sweepAngle)
    }

    internal fun updateItemPropertiesAfterRotation(rotation: Float) {
        startingAngle = startingAngle.plus(rotation).rem(RouletteWheel.CIRCLE_SIZE)
        val calculatedEndingAngle = startingAngle.plus(sweepAngle).rem(RouletteWheel.CIRCLE_SIZE)
        endingAngle =
            if (calculatedEndingAngle == 0f) RouletteWheel.CIRCLE_SIZE else calculatedEndingAngle
    }

    internal fun reset(index: Int) = updateItemProperties(index, sweepAngle)
}