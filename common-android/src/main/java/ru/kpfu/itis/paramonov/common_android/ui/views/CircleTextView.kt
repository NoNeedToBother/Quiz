package ru.kpfu.itis.paramonov.common_android.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import ru.kpfu.itis.paramonov.common_android.R
import ru.kpfu.itis.paramonov.core.utils.dpToPx
import kotlin.math.max

class CircleTextView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatTextView(ctx, attrs, defStyleAttr) {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    init {
        gravity = Gravity.CENTER
        setPadding(context.dpToPx(6f).toInt(), 0, context.dpToPx(6f).toInt(), 0)
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.CircleTextView).apply {
                try {
                    circlePaint.color = getColor(R.styleable.CircleTextView_color, Color.RED)
                } finally {
                    recycle()
                }
            }
        }
    }

    private var radius: Float? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = max(measuredHeight, measuredWidth)
        radius = size / 2f
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        radius?.let {
            canvas.drawCircle(it, it, it, circlePaint)
        }
        super.onDraw(canvas)
    }
}