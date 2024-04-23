package ru.kpfu.itis.paramonov.common_android.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import ru.kpfu.itis.paramonov.common_android.R
import ru.kpfu.itis.paramonov.common_android.utils.toPx
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class StopwatchView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(ctx, attrs, defStyleAttr) {

    var time: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    private val strokePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = context.toPx(4f)
        style = Paint.Style.STROKE
    }

    private val arrowPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = context.toPx(2f)
        style = Paint.Style.FILL
    }

    private val fillPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    init {
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.StopwatchView).apply {
                try {
                    time = getInt(R.styleable.StopwatchView_time, 0)
                    strokePaint.color = getColor(R.styleable.StopwatchView_strokeColor, Color.BLACK)
                    arrowPaint.color = getColor(R.styleable.StopwatchView_strokeColor, Color.BLACK)
                    fillPaint.color = getColor(R.styleable.StopwatchView_android_fillColor, Color.GRAY)
                } finally {
                    recycle()
                }
            }

        }
    }

    private val center get() = width / 2f

    private val radius
        get() = (width - strokePaint.strokeWidth) / 2f

    private val angle: Float
        get() {
            var pure = PI * time / 30f
            val full = PI * 2
            while(pure > full) pure -= full
            return pure.toFloat()
        }

    private val currentX get() = center + radius * sin(angle)

    private val currentY get() = radius * (1 - cos(angle))

    private val path = Path().apply {
        onPolygonPreDraw()
    }

    private fun Path.onPolygonPreDraw() {
        moveTo(center, center)
        lineTo(center, 0f)
        lineTo(2 * center, 0f)
        if (angle > PI / 2) lineTo(2 * center, 2 * center)
        if (angle > PI) lineTo(0f, 2 * center)
        if (angle > 3 * PI / 2) lineTo(0f, 0f)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path.apply {
            reset()
            onPolygonPreDraw()
            lineTo(currentX, currentY)
            close()
        }
        canvas.save()
        canvas.clipPath(path)
        canvas.drawCircle(center, center, radius, fillPaint)
        canvas.restore()

        canvas.drawCircle(center, center, radius, strokePaint)
        canvas.drawLine(center, center, currentX, currentY, arrowPaint)
    }
}