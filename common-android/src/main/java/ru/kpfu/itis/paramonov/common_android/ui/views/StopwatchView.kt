package ru.kpfu.itis.paramonov.common_android.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.kpfu.itis.paramonov.common_android.R
import ru.kpfu.itis.paramonov.common_android.utils.fromDp

class StopwatchView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(ctx, attrs, defStyleAttr) {

    var time: Int = 0
        get() = field
        set(value) {
            field = value
            invalidate()
        }

    init {
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.StopwatchView).apply {
                try {
                    time = getInt(R.styleable.StopwatchView_time, 0)
                } finally {
                    recycle()
                }
            }

        }
    }


    private val strokePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 4f.fromDp(context)
        style = Paint.Style.STROKE
    }

    private val arrowPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f.fromDp(context)
        style = Paint.Style.FILL
    }

    private val center
        get() = width / 2f

    private val radius
        get() = (width - strokePaint.strokeWidth) / 2f

    private val angle
        get() = Math.PI * time / 30f

    private val currentX
        get() = center + radius * Math.sin(angle).toFloat()

    private val currentY
        get() = radius * (1 - Math.cos(angle)).toFloat()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(center, center, radius, strokePaint)
        canvas.drawLine(center, center, currentX, currentY, arrowPaint)
    }
}