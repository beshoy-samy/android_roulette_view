package com.beshoy.roulette.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.beshoy.roulette.R
import com.beshoy.roulette.databinding.RouletteWheelViewBinding
import com.beshoy.roulette.models.RouletteWheelItemModel

class RouletteWheelView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private lateinit var binding: RouletteWheelViewBinding
    private var padding = ZERO

    init {
        val sidesPadding = maxOf(paddingStart, paddingEnd)
        val topBottomPadding = maxOf(paddingTop, paddingBottom)
        padding = maxOf(sidesPadding, topBottomPadding)
        initView(context, attributeSet)
    }

    private fun initView(context: Context, attributeSet: AttributeSet) {
        binding = RouletteWheelViewBinding.inflate(LayoutInflater.from(context), this, true)
        bindAttributes(attributeSet)
    }

    private fun bindAttributes(attributeSet: AttributeSet?) {
        attributeSet?.let {
            val array = context.theme.obtainStyledAttributes(
                attributeSet, R.styleable.RouletteWheelView, ZERO, ZERO
            )
            try {
                val strokeWidth =
                    array.getDimension(R.styleable.RouletteWheelView_strokeWidth, ZERO_F)
                binding.rouletteWheel.wheelStrokeWidth = strokeWidth
                val strokeColorRes =
                    array.getResourceId(R.styleable.RouletteWheelView_strokeColor, NONE)
                binding.rouletteWheel.wheelStrokeColorRes = strokeColorRes
                binding.rouletteWheel.padding = padding
            } finally {
                array.recycle()
            }
        }
    }

    fun setRouletteWheelItems(wheelItems: List<RouletteWheelItemModel>) =
        binding.rouletteWheel.setRouletteWheelItems(wheelItems)

    companion object {

        private const val ZERO = 0
        private const val ZERO_F = 0f
        private const val NONE = -1
    }

}