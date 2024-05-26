package ru.kpfu.itis.paramonov.common_android.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import ru.kpfu.itis.paramonov.common_android.R
import ru.kpfu.itis.paramonov.common_android.utils.dpToPx
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
        strokeWidth = context.dpToPx(4f)
        style = Paint.Style.STROKE
    }

    private val arrowPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = context.dpToPx(2f)
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
                time = getInt(R.styleable.StopwatchView_time, 0)
                strokePaint.color = getColor(R.styleable.StopwatchView_strokeColor, Color.BLACK)
                arrowPaint.color = getColor(R.styleable.StopwatchView_strokeColor, Color.BLACK)
                fillPaint.color = getColor(R.styleable.StopwatchView_android_fillColor, Color.GRAY)
                recycle()
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

    private val currentX get() = center + center * sin(angle)

    private val currentY get() = center * (1 - cos(angle))

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path.apply {
            reset()
            onPolygonPreDraw()
            lineTo(currentX, currentY)
            close()
        }
        canvas.withSave {
            canvas.clipPath(path)
            canvas.drawCircle(center, center, radius, fillPaint)
        }

        canvas.drawCircle(center, center, radius, strokePaint)
        canvas.drawClockMarks()
        canvas.drawLine(center, center, currentX, currentY, arrowPaint)
    }

    private fun Canvas.drawClockMarks() {
        drawLine(center, 0f, center, 2 * radius * CLOCK_MARK_RADIUS_PORTION, arrowPaint)
        drawLine( 2 * center, center, 2 * center * (1 - CLOCK_MARK_RADIUS_PORTION), center, arrowPaint)
        drawLine(center, 2 * center, center, 2 * center * (1 - CLOCK_MARK_RADIUS_PORTION), arrowPaint)
        drawLine(0f, center, 2 * radius * CLOCK_MARK_RADIUS_PORTION, center, arrowPaint)
    }

    override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState()
        val bundle = Bundle().apply {
            putParcelable(BUNDLE_SUPER_STATE_KEY, super.onSaveInstanceState())
            putInt(BUNDLE_TIME_KEY, time)
        }
        super.onSaveInstanceState()
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var viewState = state
        if (state is Bundle) {
            time = state.getInt(BUNDLE_TIME_KEY)
            viewState = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU)
                state.getParcelable(BUNDLE_SUPER_STATE_KEY, Parcelable::class.java)
            else state.getParcelable(BUNDLE_SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(viewState)
    }

    companion object {
        const val BUNDLE_TIME_KEY = "time"
        const val BUNDLE_SUPER_STATE_KEY = "superState"
        const val CLOCK_MARK_RADIUS_PORTION = 0.2f
    }
}