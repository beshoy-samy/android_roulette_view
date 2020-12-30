package com.beshoy.roulette.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.beshoy.roulette.R
import com.beshoy.roulette.models.RouletteWheelItemModel

class RouletteWheelView(
    context: Context,
    attributes: AttributeSet? = null
) : View(context, attributes) {

    private val rouletteWheelItems = mutableListOf<RouletteWheelItemModel>()
    private var circleSize = 0
    private var wheelItemsRange = RectF()
    private var circleCenter = 0f
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val rouletteWheelItemPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var wheelStrokeWidth = 0
    private var wheelStrokeColorRes = NONE

    init {
        bindAttributes(attributes)
    }

    private fun bindAttributes(attributeSet: AttributeSet?) {
        attributeSet?.let {
            val array = context.theme.obtainStyledAttributes(
                attributeSet, R.styleable.RouletteWheelView,
                0, 0
            )
            try {
                val strokeWidth =
                    array.getResourceId(R.styleable.RouletteWheelView_strokeWidth, ZERO)
                wheelStrokeWidth = strokeWidth
                val strokeColorRes =
                    array.getResourceId(R.styleable.RouletteWheelView_strokeColor, NONE)
                wheelStrokeColorRes = strokeColorRes
            } finally {
                array.recycle()
            }
        }
    }

    fun setRouletteWheelItems(wheelItems: List<RouletteWheelItemModel>) {
        rouletteWheelItems.clear()
        rouletteWheelItems.addAll(wheelItems)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        circleSize = minOf(w, h)
        wheelItemsRange =
            RectF(
                getRouletteWheelMargins(),
                getRouletteWheelMargins(),
                getWheelItemsSize(),
                getWheelItemsSize()
            )
        circleCenter = circleSize.div(other = 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(circleCenter, circleCenter, circleCenter,
            circlePaint.apply {
                if (wheelStrokeColorRes != NONE) color =
                    ContextCompat.getColor(context, wheelStrokeColorRes)
            }
        )
        drawWheelItems(canvas)
    }

    private fun drawWheelItems(canvas: Canvas) {
        if (rouletteWheelItems.isEmpty()) return
        val itemSize = CIRCLE_SIZE.div(rouletteWheelItems.size)
        rouletteWheelItems.forEachIndexed { index, wheelItemModel ->
            updateRouletteWheelItemPaint(wheelItemModel)
            canvas.drawArc(
                wheelItemsRange,
                itemSize.times(index),
                itemSize,
                true,
                rouletteWheelItemPaint
            )
        }
    }

    private fun updateRouletteWheelItemPaint(wheelItemModel: RouletteWheelItemModel) {
        rouletteWheelItemPaint.color =
            ContextCompat.getColor(context, wheelItemModel.backgroundColor)
    }

    private fun getRouletteWheelMargins() = wheelStrokeWidth.toFloat()

    private fun getWheelItemsSize() = circleSize.minus(getRouletteWheelMargins())

    companion object {

        private const val CIRCLE_SIZE = 360f
        private const val ZERO = 0
        private const val NONE = -1
    }
}