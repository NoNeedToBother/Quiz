package ru.kpfu.itis.paramonov.common_android.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.withSave
import ru.kpfu.itis.paramonov.common_android.R
import ru.kpfu.itis.paramonov.common_android.utils.setAndUpdate
import ru.kpfu.itis.paramonov.common_android.utils.toPx

class GraphView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(ctx, attrs, defStyleAttr) {

    private val fillPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
    }
    private val graphStrokePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = context.toPx(2f)
        style = Paint.Style.STROKE
    }
    private val dotPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
    private var dotSize = ctx.toPx(4f)
        set(value) {
            setAndUpdate { field = value }
        }

    private var dotColor
        get() = dotPaint.color
        set(value) {
            setAndUpdate { dotPaint.color = value }
        }
    private var graphStrokeColor
        get() = graphStrokePaint.color
        set(value) {
            setAndUpdate { graphStrokePaint.color = value }
        }
    private var graphStrokeWidth
        get() = graphStrokePaint.strokeWidth
        set(value) {
            setAndUpdate { graphStrokePaint.strokeWidth = value }
        }
    private var graphFillColor
        get() = fillPaint.color
        set(value) {
            setAndUpdate { fillPaint.color = value }
        }
    private var gradient = false
        set(value) {
            setAndUpdate { field = value }
        }

    private val dots = mutableListOf<Dot>()

    init {
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.GraphView).apply {
                fillPaint.color = getColor(R.styleable.GraphView_graphFillColor, Color.GRAY)
                graphStrokePaint.color = getColor(R.styleable.GraphView_graphStrokeColor, Color.BLACK)
                graphStrokePaint.strokeWidth = getDimension(R.styleable.GraphView_graphStrokeWidth, ctx.toPx(2f))
                dotPaint.color = getColor(R.styleable.GraphView_dotColor, Color.BLACK)
                gradient = getBoolean(R.styleable.GraphView_gradient, false)
                recycle()
            }
        }
    }

    private val xInterval: Double get() {
        return if (dots.isNotEmpty()) {
            dots[dots.size - 1].x - dots[0].x
        } else -1.0
    }

    private val yInterval: Double get() {
        return if (dots.isNotEmpty()) {
            dots.max().y - dots.min().y
        } else -1.0
    }

    private val gridHeight: Float get() {
        return if (dots.isNotEmpty()) {
            (1 - GRAPH_MARGIN - GRAPH_BOTTOM_MARGIN) * height / yInterval.toFloat()
        } else -1f
    }

    private val gridWidth: Float get() {
        return if (dots.isNotEmpty()) {
            (1 - 2 * GRAPH_MARGIN) * width / xInterval.toFloat()
        } else -1f
    }

    private val graphOffsetX: Float get() = width * GRAPH_MARGIN
    private val graphOffsetY: Float get() = height * GRAPH_MARGIN

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withSave {
            clipGraphArea()
            drawGraph()
        }
    }

    private fun Canvas.drawGraph() {
        for (dot in dots) {
            drawCircle(getGraphPositionX(dot.x), getGraphPositionY(dot.y), dotSize, dotPaint)
        }
    }

    private fun getGraphPositionX(x: Double): Float {
        return x.toFloat() * gridWidth + graphOffsetX
    }
    private fun getGraphPositionY(y: Double): Float {
        return y.toFloat() * gridHeight + graphOffsetY
    }

    private fun Canvas.clipGraphArea() {
        val path = Path().apply {
            moveTo(width * GRAPH_MARGIN, height * (1 - GRAPH_BOTTOM_MARGIN))
            lineTo(width * GRAPH_MARGIN, height * GRAPH_MARGIN)
            lineTo(width * (1 - GRAPH_MARGIN), height * GRAPH_MARGIN)
            lineTo(width * (1 - GRAPH_MARGIN), height * (1 - GRAPH_BOTTOM_MARGIN))
            close()
        }
        clipPath(path)
    }

    fun provideValues(vararg values: Pair<Double, Double>) {
        provideValues(listOf(*values))
    }

    fun provideValues(values: List<Pair<Double, Double>>) {
        Log.i("a", values.toString())
        values.sortedBy {
            it.first
        }.forEach {
            addDot(it.first, it.second)
        }
        invalidate()
    }

    fun provideValues(values: Map<Double, Double>) {
        values.toSortedMap()
            .forEach {
                addDot(it.key, it.value)
            }
        invalidate()
    }

    private fun addDot(x: Double, y: Double) {
        val dot = Dot(x, y)
        dots.add(dot)
    }

    private data class Dot(val x: Double, val y: Double): Comparable<Dot> {
        override fun compareTo(other: Dot): Int {
            return y.compareTo(other.y)
        }

    }

    companion object {
        private const val GRAPH_MARGIN = 0.1f
        private const val GRAPH_BOTTOM_MARGIN = 0.2f
    }
}