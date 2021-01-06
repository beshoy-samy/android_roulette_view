package com.beshoy.roulette.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.beshoy.roulette.R
import com.beshoy.roulette.models.RouletteWheelItemModel
import com.beshoy.roulette.utils.onMeasureRouletteViewSize
import kotlin.math.PI

internal class RouletteWheel(
    context: Context,
    attributes: AttributeSet? = null
) : View(context, attributes) {

    internal val rouletteWheelItems = mutableListOf<RouletteWheelItemModel>()
    internal var rouletteViewSize = ZERO
    private var wheelItemsRange = RectF()
    private var rouletteViewCenter = ZERO_F
    private val rouletteWheelRadius: Float
        get() = rouletteViewCenter.minus(padding).minus(wheelStrokeWidth)
    private val rouletteViewPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            isDither = true
        }
    private val rouletteWheelItemPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isDither = true
        }
    private val rouletteWheelArcsDivider =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isDither = true
        }
    private val wheelItemTextPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isDither = true
        }
    internal var wheelItemTextColor = R.color.white
    internal var wheelItemTextSize = DEFAULT_ITEM_TEXT_SIZE
    internal var wheelStrokeWidth = ZERO_F
    internal var wheelStrokeColorRes = R.color.black
    internal var padding = ZERO
    internal var showItemsDividerBullet = false
    internal var itemsDividerBulletColor = R.color.black
    internal var itemsDividerBulletSize = ITEMS_DIVIDER_BULLET_SIZE

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && elevation > ZERO_F)
            showElevation(elevation, rouletteViewSize, rouletteViewCenter)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showElevation(elevation: Float, rouletteViewSize: Int, rouletteViewCenter: Float) {
        outlineProvider =
            RouletteWheelOutlineProvider(padding, elevation, rouletteViewSize, rouletteViewCenter)
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
        drawWheelItemsDivider(canvas)
    }

    private fun drawWheelStroke(canvas: Canvas) {
        if (wheelStrokeWidth == ZERO_F) return
        rouletteViewPaint.strokeWidth = wheelStrokeWidth
        rouletteViewPaint.color = ContextCompat.getColor(context, wheelStrokeColorRes)
        canvas.drawCircle(
            rouletteViewCenter,
            rouletteViewCenter,
            rouletteWheelRadius,
            rouletteViewPaint
        )
    }

    private fun drawWheelItems(canvas: Canvas) {
        if (rouletteWheelItems.isEmpty()) return
        val itemSweepAngle = CIRCLE_SIZE.div(rouletteWheelItems.size)
        rouletteWheelItems.forEachIndexed { index, wheelItemModel ->
            updateRouletteWheelItemPaint(wheelItemModel)
            wheelItemModel.updateItemProperties(index, itemSweepAngle)
            wheelItemModel.calculateStartingPointCoordinates(
                rouletteViewCenter,
                rouletteWheelRadius
            )
            drawArch(canvas, wheelItemModel)
            drawText(canvas, wheelItemModel)
        }
    }

    private fun drawArch(canvas: Canvas, wheelItemModel: RouletteWheelItemModel) =
        canvas.drawArc(
            wheelItemsRange,
            wheelItemModel.startingAngle,
            wheelItemModel.sweepAngle,
            true,
            rouletteWheelItemPaint
        )

    private fun drawText(canvas: Canvas, wheelItemModel: RouletteWheelItemModel) {
        if (wheelItemModel.text.isNullOrEmpty()) return
        wheelItemTextPaint.color = ContextCompat.getColor(context, wheelItemTextColor)
        wheelItemTextPaint.textSize = wheelItemTextSize
        val textPath = Path()
        textPath.addArc(wheelItemsRange, wheelItemModel.startingAngle, wheelItemModel.sweepAngle)
        val textHalfWidth = wheelItemTextPaint.measureText(wheelItemModel.text).div(2)
        val hOffset =
            rouletteWheelRadius.times(PI).div(rouletteWheelItems.size).minus(textHalfWidth)
                .toFloat()
        val vOffset = rouletteWheelRadius.times(0.25).toFloat()
        canvas.drawTextOnPath(wheelItemModel.text, textPath, hOffset, vOffset, wheelItemTextPaint)
    }

    private fun drawWheelItemsDivider(canvas: Canvas) {
        if (showItemsDividerBullet) {
            rouletteWheelArcsDivider.color =
                ContextCompat.getColor(context, itemsDividerBulletColor)
            rouletteWheelItems.forEach { wheelItemModel ->
                drawArchDividerBullet(canvas, wheelItemModel)
            }
        }
    }

    private fun drawArchDividerBullet(canvas: Canvas, wheelItemModel: RouletteWheelItemModel) {
        canvas.drawCircle(
            wheelItemModel.startingPointCoordinates.first,
            wheelItemModel.startingPointCoordinates.second,
            itemsDividerBulletSize,
            rouletteWheelArcsDivider
        )
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
        val rotation =
            if (targetRotation < ZERO) CIRCLE_SIZE.plus(targetRotation) else targetRotation
        rouletteWheelItems.forEach { wheelItemModel ->
            wheelItemModel.updateItemPropertiesAfterRotation(rotation)
            wheelItemModel.calculateStartingPointCoordinates(
                rouletteViewCenter,
                rouletteWheelRadius
            )
        }
    }

    internal fun reset() {
        rouletteWheelItems.forEachIndexed { index, wheelItemModel ->
            wheelItemModel.reset(index)
            wheelItemModel.calculateStartingPointCoordinates(
                rouletteViewCenter,
                rouletteWheelRadius
            )
        }
    }

    companion object {

        internal const val CIRCLE_SIZE = 360F
        internal const val ITEMS_DIVIDER_BULLET_SIZE = 15F
        internal const val DEFAULT_ITEM_TEXT_SIZE = 15F
        private const val ZERO = 0
        private const val ZERO_F = 0F
    }
}