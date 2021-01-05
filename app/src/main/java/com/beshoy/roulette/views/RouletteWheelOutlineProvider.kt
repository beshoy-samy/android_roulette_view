package com.beshoy.roulette.views

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class RouletteWheelOutlineProvider(
    private val padding: Int,
    private val elevation: Float,
    private val rouletteViewSize: Int,
    private val rouletteViewCenter: Float
) : ViewOutlineProvider() {

    override fun getOutline(view: View, outline: Outline) {
        outline.alpha = ELEVATION_ALPHA
        outline.setRoundRect(
            padding,
            padding,
            rouletteViewSize.minus(padding),
            rouletteViewSize.minus(padding),
            rouletteViewCenter.plus(elevation)
        )
    }

    companion object {
        private const val ELEVATION_ALPHA = 0.4F
    }
}