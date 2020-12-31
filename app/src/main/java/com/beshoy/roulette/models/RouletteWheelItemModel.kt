package com.beshoy.roulette.models

import androidx.annotation.ColorRes

data class RouletteWheelItemModel(@ColorRes val backgroundColor: Int) {

    internal var startingAngle = 0f
    internal var endingAngle = 0f
    internal var sweepAngle = 0f

    internal fun updateItemProperties(index: Int, sweepAngle: Float) {
        this.sweepAngle = sweepAngle
        startingAngle = sweepAngle.times(index)
        endingAngle = startingAngle.plus(sweepAngle)
    }
}