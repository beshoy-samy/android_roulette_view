package com.beshoy.roulette.views

import android.animation.Animator
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.beshoy.roulette.R
import com.beshoy.roulette.databinding.RouletteWheelViewBinding
import com.beshoy.roulette.models.RouletteWheelItemModel
import timber.log.Timber
import kotlin.random.Random

class RouletteWheelView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private lateinit var binding: RouletteWheelViewBinding
    private var padding = ZERO
    private var startingAngle = STARTING_ANGLE
    private var lastRotation = ZERO_F

    var rouletteSpinSecondsDuration = DEFAULT_SPIN_DURATION
    var rouletteSpinSpeed = DEFAULT_SPINNING_SPEED

    init {
        clipToPadding = false
        getCorrectViewPadding()
        initView(context, attributeSet)
    }

    private fun getCorrectViewPadding() {
        val sidesPadding = maxOf(paddingStart, paddingEnd)
        val topBottomPadding = maxOf(paddingTop, paddingBottom)
        padding = maxOf(sidesPadding, topBottomPadding)
    }

    private fun initView(context: Context, attributeSet: AttributeSet) {
        binding = RouletteWheelViewBinding.inflate(LayoutInflater.from(context), this, true)
        bindAttributes(attributeSet)
    }

    private fun bindAttributes(attributeSet: AttributeSet?) {
        attributeSet?.let {
            val array =
                context.theme.obtainStyledAttributes(
                    attributeSet,
                    R.styleable.RouletteWheelView,
                    ZERO,
                    ZERO
                )
            try {
                handleRouletteWheelAttributes(array)
                handleSpinButtonAttributes(array)
            } finally {
                array.recycle()
            }
        }
    }

    private fun handleRouletteWheelAttributes(array: TypedArray) {
        val strokeWidth =
            array.getDimension(R.styleable.RouletteWheelView_strokeWidth, ZERO_F)
        binding.rouletteWheel.wheelStrokeWidth = strokeWidth
        val strokeColorRes =
            array.getResourceId(R.styleable.RouletteWheelView_strokeColor, NONE)
        binding.rouletteWheel.wheelStrokeColorRes = strokeColorRes
        val spinSpeed =
            array.getInt(R.styleable.RouletteWheelView_spinSpeed, DEFAULT_SPINNING_SPEED)
        rouletteSpinSpeed = spinSpeed
        val spinDuration = array.getInt(
            R.styleable.RouletteWheelView_spinSecondsDuration,
            DEFAULT_SPIN_DURATION
        )
        rouletteSpinSecondsDuration = spinDuration
        val showItemsDividerBullet =
            array.getBoolean(R.styleable.RouletteWheelView_showItemsDividerBullet, false)
        binding.rouletteWheel.showItemsDividerBullet = showItemsDividerBullet
        val itemsDividerBulletColor =
            array.getResourceId(R.styleable.RouletteWheelView_itemsDividerBulletColor, NONE)
        if (itemsDividerBulletColor != NONE)
            binding.rouletteWheel.itemsDividerBulletColor = itemsDividerBulletColor
        val itemsDividerBulletSize =
            array.getDimension(
                R.styleable.RouletteWheelView_itemsDividerBulletSize,
                RouletteWheel.ITEMS_DIVIDER_BULLET_SIZE
            )
        binding.rouletteWheel.itemsDividerBulletSize = itemsDividerBulletSize
        val itemTextColor =
            array.getResourceId(R.styleable.RouletteWheelView_itemTextColor, NONE)
        if (itemTextColor != NONE) binding.rouletteWheel.wheelItemTextColor = itemTextColor
        val itemTextSize =
            array.getDimension(
                R.styleable.RouletteWheelView_itemTextSize,
                RouletteWheel.DEFAULT_ITEM_TEXT_SIZE
            )
        binding.rouletteWheel.wheelItemTextSize = itemTextSize
        val elevation =
            array.getDimension(R.styleable.RouletteWheelView_android_elevation, ZERO_F)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            binding.rouletteWheel.elevation = elevation
        binding.rouletteWheel.padding = padding
    }

    private fun handleSpinButtonAttributes(array: TypedArray) {
        val showButton = array.getBoolean(R.styleable.RouletteWheelView_showSpinBtn, false)
        binding.spinBtn.isVisible = showButton
        val buttonTitleRes = array.getResourceId(R.styleable.RouletteWheelView_spinBtnTitle, NONE)
        if (buttonTitleRes != NONE) binding.spinBtn.setText(buttonTitleRes)
        val buttonColorRes = array.getResourceId(R.styleable.RouletteWheelView_spinBtnColor, NONE)
        if (buttonColorRes != NONE)
            binding.spinBtn.setBackgroundColor(ContextCompat.getColor(context, buttonColorRes))
        val buttonTextColorRes =
            array.getResourceId(R.styleable.RouletteWheelView_spinBtnTextColor, NONE)
        if (buttonTextColorRes != NONE)
            binding.spinBtn.setTextColor(ContextCompat.getColor(context, buttonTextColorRes))
        val spinButtonSizeRatio =
            array.getFloat(R.styleable.RouletteWheelView_spinBtnSizeRatio, SPIN_BUTTON_SIZE_RATIO)
    }

    fun setRouletteWheelItems(wheelItems: List<RouletteWheelItemModel>) =
        binding.rouletteWheel.setRouletteWheelItems(wheelItems)

    fun setSpinButtonClickListener(listener: OnClickListener) {
        binding.spinBtn.setOnClickListener(listener)
    }

    fun spinTheWheel() {
        val randomTarget = Random.nextInt(0, binding.rouletteWheel.rouletteWheelItems.size)
        spinTheWheelToItem(randomTarget)
    }

    fun spinTheWheelToItem(index: Int) {
        val targetItem = binding.rouletteWheel.rouletteWheelItems[index]
        val targetItemCenter = targetItem.startingAngle.plus(targetItem.sweepAngle.div(2))
        val targetRotation = startingAngle.minus(targetItemCenter)
        Timber.i("targeting index $index with rotation of $targetRotation")
        binding.rouletteWheel.animate()
            .apply {
                duration = rouletteSpinSecondsDuration.times(1000L)
                interpolator = DecelerateInterpolator()
                rotation(
                    RouletteWheel.CIRCLE_SIZE.times(rouletteSpinSpeed).plus(targetRotation)
                        .plus(lastRotation)
                )
                setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        binding.rouletteWheel.updateItemsAfterRotation(targetRotation)
                        binding.rouletteWheel.clearAnimation()
                        updateLastRotationValue(targetRotation)
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}

                })
            }
            .start()
    }

    private fun updateLastRotationValue(targetRotation: Float) {
        lastRotation = lastRotation.plus(targetRotation).rem(RouletteWheel.CIRCLE_SIZE)
    }

    fun reset() {
        binding.rouletteWheel.rotation = ZERO_F
        lastRotation = ZERO_F
        binding.rouletteWheel.reset()
    }

    companion object {

        private const val ZERO = 0
        private const val ZERO_F = 0f
        private const val NONE = -1
        private const val SPIN_BUTTON_SIZE_RATIO = 0.3F
        private const val DEFAULT_SPIN_DURATION = 6
        private const val DEFAULT_SPINNING_SPEED = 10
        private const val STARTING_ANGLE = 270f
    }

}