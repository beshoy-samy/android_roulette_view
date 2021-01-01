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
import com.beshoy.roulette.utils.onMeasureRouletteViewSize

internal class RouletteWheel(
    context: Context,
    attributes: AttributeSet? = null
) : View(context, attributes) {

    internal val rouletteWheelItems = mutableListOf<RouletteWheelItemModel>()
    private var rouletteViewSize = ZERO
    private var wheelItemsRange = RectF()
    private var rouletteViewCenter = ZERO_F
    private val rouletteViewPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            isDither = true
        }
    private val rouletteWheelItemPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    internal var wheelStrokeWidth = ZERO_F
    internal var wheelStrokeColorRes = R.color.black
    internal var padding = ZERO

    fun setRouletteWheelItems(wheelItems: List<RouletteWheelItemModel>) {
        rouletteWheelItems.clear()
        rouletteWheelItems.addAll(wheelItems)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rouletteViewSize = minOf(w, h)
        wheelItemsRange = getWheelItemRange()
        rouletteViewCenter = rouletteViewSize.div(other = 2).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val rouletteViewSize =
            onMeasureRouletteViewSize(
                widthMeasureSpec,
                heightMeasureSpec,
                resources.getDimension(R.dimen.roulette_minimum_size),
                wheelStrokeWidth,
                padding
            )
        setMeasuredDimension(rouletteViewSize, rouletteViewSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWheelStroke(canvas)
        drawWheelItems(canvas)
    }

    private fun drawWheelStroke(canvas: Canvas) {
        if (wheelStrokeWidth == ZERO_F) return
        rouletteViewPaint.strokeWidth = wheelStrokeWidth
        rouletteViewPaint.color = ContextCompat.getColor(context, wheelStrokeColorRes)
        canvas.drawCircle(
            rouletteViewCenter,
            rouletteViewCenter,
            rouletteViewCenter.minus(wheelStrokeWidth).minus(padding),
            rouletteViewPaint
        )
    }

    private fun drawWheelItems(canvas: Canvas) {
        if (rouletteWheelItems.isEmpty()) return
        val itemSweepAngle = CIRCLE_SIZE.div(rouletteWheelItems.size)
        rouletteWheelItems.forEachIndexed { index, wheelItemModel ->
            updateRouletteWheelItemPaint(wheelItemModel)
            wheelItemModel.updateItemProperties(index, itemSweepAngle)
            canvas.drawArc(
                wheelItemsRange,
                wheelItemModel.startingAngle,
                wheelItemModel.sweepAngle,
                true,
                rouletteWheelItemPaint
            )
        }
    }

    private fun updateRouletteWheelItemPaint(wheelItemModel: RouletteWheelItemModel) {
        rouletteWheelItemPaint.color =
            ContextCompat.getColor(context, wheelItemModel.backgroundColor)
    }

    private fun getWheelItemRange() =
        RectF(
            wheelStrokeWidth.plus(padding),
            wheelStrokeWidth.plus(padding),
            rouletteViewSize.minus(wheelStrokeWidth).minus(padding),
            rouletteViewSize.minus(wheelStrokeWidth).minus(padding)
        )

    internal fun updateItemsAfterRotation(targetRotation: Float) {
        val rotation = if (targetRotation < 0) CIRCLE_SIZE.plus(targetRotation) else targetRotation
        rouletteWheelItems.forEach { wheelItemModel ->
            wheelItemModel.updateItemPropertiesAfterRotation(rotation)
        }
    }

    internal fun reset() {
        rouletteWheelItems.forEachIndexed { index, wheelItemModel ->
            wheelItemModel.reset(index)
        }
    }

    companion object {

        internal const val CIRCLE_SIZE = 360f
        private const val ZERO = 0
        private const val ZERO_F = 0f
    }
}