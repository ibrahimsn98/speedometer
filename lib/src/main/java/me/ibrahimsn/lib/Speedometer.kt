package me.ibrahimsn.lib

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.animation.doOnEnd
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Speedometer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Attribute Defaults
    private var _maxSpeed = 60

    @Dimension
    private var _borderSize = 36f

    @Dimension
    private var _textGap = 50f

    @ColorInt
    private var _borderColor = Color.parseColor("#402c47")

    @ColorInt
    private var _fillColor = Color.parseColor("#d83a78")

    @ColorInt
    private var _textColor = Color.parseColor("#f5f5f5")

    private var _metricText = "km/h"

    // Dynamic Values
    private val indicatorBorderRect = RectF()

    private val tickBorderRect = RectF()

    private val textBounds = Rect()

    private var angle = MIN_ANGLE

    private var speed = 0

    // Dimension Getters
    private val centerX get() = width / 2f

    private val centerY get() = height / 2f

    // Core Attributes
    var maxSpeed: Int
        get() = _maxSpeed
        set(value) {
            _maxSpeed = value
            invalidate()
        }

    var borderSize: Float
        @Dimension get() = _borderSize
        set(@Dimension value) {
            _borderSize = value
            paintIndicatorBorder.strokeWidth = value
            paintIndicatorFill.strokeWidth = value
            invalidate()
        }

    var textGap: Float
        @Dimension get() = _textGap
        set(@Dimension value) {
            _textGap = value
            invalidate()
        }

    var metricText: String
        get() = _metricText
        set(value) {
            _metricText = value
            invalidate()
        }

    var borderColor: Int
        @ColorInt get() = _borderColor
        set(@ColorInt value) {
            _borderColor = value
            paintIndicatorBorder.color = value
            paintTickBorder.color = value
            paintMajorTick.color = value
            paintMinorTick.color = value
            invalidate()
        }

    var fillColor: Int
        @ColorInt get() = _fillColor
        set(@ColorInt value) {
            _fillColor = value
            paintIndicatorFill.color = value
            invalidate()
        }

    var textColor: Int
        @ColorInt get() = _textColor
        set(@ColorInt value) {
            _textColor = value
            paintTickText.color = value
            paintSpeed.color = value
            paintMetric.color = value
            invalidate()
        }

    // Paints
    private val paintIndicatorBorder = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = borderColor
        strokeWidth = borderSize
        strokeCap = Paint.Cap.ROUND
    }

    private val paintIndicatorFill = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = fillColor
        strokeWidth = borderSize
        strokeCap = Paint.Cap.ROUND
    }

    private val paintTickBorder = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = borderColor
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
    }

    private val paintMajorTick = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = borderColor
        strokeWidth = MAJOR_TICK_WIDTH
        strokeCap = Paint.Cap.BUTT
    }

    private val paintMinorTick = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = borderColor
        strokeWidth = MINOR_TICK_WIDTH
        strokeCap = Paint.Cap.BUTT
    }

    private val paintTickText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = textColor
        textSize = 40f
    }

    private val paintSpeed = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = textColor
        textSize = 260f
    }

    private val paintMetric = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = textColor
        textSize = 50f
    }

    // Animators
    private val animator = ValueAnimator.ofFloat().apply {
        interpolator = AccelerateDecelerateInterpolator()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        indicatorBorderRect.set(
            borderSize / 2,
            borderSize / 2,
            width - borderSize / 2,
            width - borderSize / 2
        )
        tickBorderRect.set(
            borderSize + TICK_MARGIN,
            borderSize + TICK_MARGIN,
            width - borderSize - TICK_MARGIN,
            width - borderSize - TICK_MARGIN
        )
    }

    init {
        obtainStyledAttributes(attrs, defStyleAttr)
    }

    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Speedometer,
            defStyleAttr,
            0
        )

        try {
            with(typedArray) {
                maxSpeed = getInt(
                    R.styleable.Speedometer_maxSpeed,
                    maxSpeed
                )
                borderSize = getDimension(
                    R.styleable.Speedometer_borderSize,
                    borderSize
                )
                textGap = getDimension(
                    R.styleable.Speedometer_textGap,
                    textGap
                )
                metricText = getString(
                    R.styleable.Speedometer_metricText
                ) ?: metricText
                borderColor = getColor(
                    R.styleable.Speedometer_borderColor,
                    borderColor
                )
                fillColor = getColor(
                    R.styleable.Speedometer_fillColor,
                    borderColor
                )
                textColor = getColor(
                    R.styleable.Speedometer_textColor,
                    borderColor
                )
            }
        } catch (e: Exception) {
            // ignored
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        renderMajorTicks(canvas)
        renderMinorTicks(canvas)
        renderBorder(canvas)
        renderBorderFill(canvas)
        renderTickBorder(canvas)
        renderSpeedAndMetricText(canvas)
    }

    private fun renderMinorTicks(canvas: Canvas) {
        for (s in MIN_SPEED..maxSpeed step 2) {
            if (s % 10 != 0) {
                canvas.drawLine(
                    centerX + (centerX - borderSize - MINOR_TICK_SIZE) * cos(mapSpeedToAngle(s).toRadian()),
                    centerY - (centerY - borderSize - MINOR_TICK_SIZE) * sin(mapSpeedToAngle(s).toRadian()),
                    centerX + (centerX - borderSize - TICK_MARGIN) * cos(mapSpeedToAngle(s).toRadian()),
                    centerY - (centerY - borderSize - TICK_MARGIN) * sin(mapSpeedToAngle(s).toRadian()),
                    paintMinorTick
                )
            }
        }
    }

    private fun renderMajorTicks(canvas: Canvas) {
        for (s in MIN_SPEED..maxSpeed step 10) {
            canvas.drawLine(
                centerX + (centerX - borderSize - MAJOR_TICK_SIZE) * cos(mapSpeedToAngle(s).toRadian()),
                centerY - (centerY - borderSize - MAJOR_TICK_SIZE) * sin(mapSpeedToAngle(s).toRadian()),
                centerX + (centerX - borderSize - TICK_MARGIN) * cos(mapSpeedToAngle(s).toRadian()),
                centerY - (centerY - borderSize - TICK_MARGIN) * sin(mapSpeedToAngle(s).toRadian()),
                paintMajorTick
            )
            canvas.drawTextCentred(
                s.toString(),
                centerX + (centerX - borderSize - MAJOR_TICK_SIZE - TICK_MARGIN - TICK_TEXT_MARGIN) * cos(mapSpeedToAngle(s).toRadian()),
                centerY - (centerY - borderSize - MAJOR_TICK_SIZE - TICK_MARGIN - TICK_TEXT_MARGIN) * sin(mapSpeedToAngle(s).toRadian()),
                paintTickText
            )
        }
    }

    private fun renderBorder(canvas: Canvas) {
        canvas.drawArc(
            indicatorBorderRect,
            140f,
            260f,
            false,
            paintIndicatorBorder
        )
    }

    private fun renderTickBorder(canvas: Canvas) {
        canvas.drawArc(
            tickBorderRect,
            START_ANGLE,
            SWEEP_ANGLE,
            false,
            paintTickBorder
        )
    }

    private fun renderBorderFill(canvas: Canvas) {
        canvas.drawArc(
            indicatorBorderRect,
            START_ANGLE,
            MIN_ANGLE - angle,
            false,
            paintIndicatorFill
        )
    }

    private fun renderSpeedAndMetricText(canvas: Canvas) {
        canvas.drawTextCentred(
            speed.toString(),
            width / 2f,
            height / 2f,
            paintSpeed
        )
        canvas.drawTextCentred(
            metricText,
            width / 2f,
            height / 2f + paintSpeed.textSize/2 + textGap,
            paintMetric
        )
    }

    private fun mapSpeedToAngle(speed: Int): Float {
        return (MIN_ANGLE + ((MAX_ANGLE - MIN_ANGLE) / (maxSpeed - MIN_SPEED)) * (speed - MIN_SPEED))
    }

    private fun mapAngleToSpeed(angle: Float): Int {
        return (MIN_SPEED + ((maxSpeed - MIN_SPEED) / (MAX_ANGLE - MIN_ANGLE)) * (angle - MIN_ANGLE)).toInt()
    }

    fun setSpeed(s: Int, d: Long, onEnd: (() -> Unit)? = null) {
        animator.apply {
            setFloatValues(mapSpeedToAngle(speed), mapSpeedToAngle(s))

            addUpdateListener {
                angle = it.animatedValue as Float
                speed = mapAngleToSpeed(angle)
                invalidate()
            }
            doOnEnd {
                onEnd?.invoke()
            }
            duration = d
            start()
        }
    }

    private fun Canvas.drawTextCentred(text: String, cx: Float, cy: Float, paint: Paint) {
        paint.getTextBounds(text, 0, text.length, textBounds)
        drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint)
    }

    private fun Float.toRadian(): Float {
        return this * (PI / 180).toFloat()
    }

    companion object {
        private const val MIN_ANGLE = 220f
        private const val MAX_ANGLE = -40f
        private const val START_ANGLE = 140f
        private const val SWEEP_ANGLE = 260f

        private const val MIN_SPEED = 0
        private const val TICK_MARGIN = 10f
        private const val TICK_TEXT_MARGIN = 30f
        private const val MAJOR_TICK_SIZE = 50f
        private const val MINOR_TICK_SIZE = 25f
        private const val MAJOR_TICK_WIDTH = 4f
        private const val MINOR_TICK_WIDTH = 2f
    }
}
