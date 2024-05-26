package ru.kpfu.itis.paramonov.common_android.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.graphics.withSave
import ru.kpfu.itis.paramonov.common_android.R
import ru.kpfu.itis.paramonov.common_android.utils.setAndUpdate
import ru.kpfu.itis.paramonov.common_android.utils.dpToPx
import ru.kpfu.itis.paramonov.common_android.utils.spToPx

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
        strokeWidth = context.dpToPx(4f)
        style = Paint.Style.STROKE
    }
    private val dotPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
    private val labelPaint = Paint().apply {
        color = Color.BLACK
        textSize = context.spToPx(8f)
    }
    var dotSize = ctx.dpToPx(6f)
        set(value) {
            setAndUpdate { field = value }
        }

    var dotColor
        get() = dotPaint.color
        set(value) {
            setAndUpdate { dotPaint.color = value }
        }
    var graphStrokeColor
        get() = graphStrokePaint.color
        set(value) {
            setAndUpdate { graphStrokePaint.color = value }
        }
    var graphStrokeWidth
        get() = graphStrokePaint.strokeWidth
        set(value) {
            setAndUpdate { graphStrokePaint.strokeWidth = value }
        }
    var graphFillColor
        get() = fillPaint.color
        set(value) {
            setAndUpdate { fillPaint.color = value }
        }
    var graphGradientColor = Color.WHITE
        set(value) {
            setAndUpdate { field = value }
        }
    var gradient = false
        set(value) {
            setAndUpdate { field = value }
        }

    var labelXAmount = LABEL_X_DEFAULT_AMOUNT
        set(value) {
            setAndUpdate { field = value }
        }
    var labelYAmount = LABEL_Y_DEFAULT_AMOUNT
        set(value) {
            setAndUpdate { field = value }
        }

    private val dots = mutableListOf<Dot>()

    init {
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.GraphView).apply {
                graphFillColor = getColor(R.styleable.GraphView_graphFillColor, Color.GRAY)
                graphStrokeColor = getColor(R.styleable.GraphView_graphStrokeColor, Color.BLACK)
                graphStrokeWidth = getDimension(R.styleable.GraphView_graphStrokeWidth, ctx.dpToPx(2f))
                dotColor = getColor(R.styleable.GraphView_dotColor, Color.BLACK)
                dotSize = getDimension(R.styleable.GraphView_dotSize, ctx.dpToPx(4f))
                gradient = getBoolean(R.styleable.GraphView_gradient, false)
                if (gradient) {
                    graphGradientColor = getColor(R.styleable.GraphView_graphGradientColor, Color.WHITE)
                }
                labelXAmount = getInt(R.styleable.GraphView_labelXAmount, LABEL_X_DEFAULT_AMOUNT)
                labelYAmount = getInt(R.styleable.GraphView_labelYAmount, LABEL_Y_DEFAULT_AMOUNT)
                recycle()
            }
        }
    }

    private val xInterval: Double get() {
        return if (dots.isNotEmpty()) {
            dots.last().x - dots.first().x
        } else -1.0
    }

    private val yInterval: Double get() {
        return if (dots.isNotEmpty()) {
            dots.max().y - dots.min().y
        } else -1.0
    }

    private val gridHeight: Float get() {
        return if (dots.isNotEmpty()) {
            graphHeight / yInterval.toFloat()
        } else -1f
    }

    private val gridWidth: Float get() {
        return if (dots.isNotEmpty()) {
            graphWidth / xInterval.toFloat()
        } else -1f
    }

    private val graphOffsetX: Float get() = width * GRAPH_MARGIN
    private val graphOffsetY: Float get() = height * GRAPH_MARGIN

    private val graphHeight: Float get() = (1 - GRAPH_MARGIN - GRAPH_BOTTOM_MARGIN) * height

    private val graphWidth: Float get() = (1 - 2 * GRAPH_MARGIN) * width

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = if (widthMode == WRAP_CONTENT || widthMode == MeasureSpec.AT_MOST) widthSize else MeasureSpec.getSize(widthMeasureSpec)
        var height = if (heightMode == WRAP_CONTENT || heightMode == MeasureSpec.AT_MOST) (width * 9 / 16) else MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            width = width.coerceAtMost(widthSize)
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = height.coerceAtMost(heightSize)
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawGraph()
        canvas.drawLabels()
    }

    private fun Canvas.drawLabels() {
        val deltaX = xInterval / labelXAmount
        val deltaY = yInterval / labelYAmount
        val labelXPosY = (1 - GRAPH_BOTTOM_MARGIN) * height + GRAPH_BOTTOM_MARGIN / 2 * height
        val labelYPosX = width * GRAPH_MARGIN / 2
        drawText("%.2f".format(dots.first().x), getGraphPositionX(dots.first().x), labelXPosY, labelPaint)
        drawText("%.2f".format(dots.min().y), labelYPosX, getGraphPositionY(dots.min().y), labelPaint)
        if (dots.size > 1 && labelXAmount > 1) {
            for (delta in 0 ..   labelXAmount) {
                if (delta == 0 || delta == labelXAmount) continue
                val deltaValue = dots.first().x + delta * deltaX
                drawText("%.2f".format(deltaValue), getGraphPositionX(deltaValue), labelXPosY, labelPaint)
            }
            for (delta in 0 ..   labelYAmount) {
                if (delta == 0 || delta == labelXAmount) continue
                val deltaValue = dots.min().y + delta * deltaY
                drawText("%.2f".format(deltaValue), labelYPosX, getGraphPositionY(deltaValue), labelPaint)
            }
            drawText("%.2f".format(dots.last().x), getGraphPositionX(dots.last().x), labelXPosY, labelPaint)
            drawText("%.2f".format(dots.max().y), labelYPosX, getGraphPositionY(dots.max().y), labelPaint)
        }
    }

    private fun Canvas.drawGraph() {
        val graphPath = Path()
        for (i in dots.indices) {
            val dot = dots[i]
            val dotPosX = getGraphPositionX(dot.x)
            val dotPosY = getGraphPositionY(dot.y)
            if (i == 0) graphPath.moveTo(dotPosX, dotPosY)
            else graphPath.lineTo(dotPosX, dotPosY)
        }
        drawPath(graphPath, graphStrokePaint)
        graphPath.closeGraphPath()
        withSave {
            clipPath(graphPath)
            fillGraph(graphPath)
        }
        drawDots()
    }

    private fun Canvas.fillGraph(path: Path) {
        if (gradient) {
            val pathBounds = RectF()
            path.computeBounds(pathBounds, true)
            fillPaint.shader = LinearGradient(
                pathBounds.centerX(), pathBounds.top,
                pathBounds.centerX(), pathBounds.bottom,
                intArrayOf(graphFillColor, graphGradientColor),
                floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
        }
        drawPaint(fillPaint)
    }

    private fun Canvas.drawDots() {
        for (dot in dots) {
            val dotPosX = getGraphPositionX(dot.x)
            val dotPosY = getGraphPositionY(dot.y)
            drawCircle(dotPosX, dotPosY, dotSize, dotPaint)
        }
    }

    private fun Path.closeGraphPath() {
        lineTo(getGraphPositionX(dots.last().x), height * (1 - GRAPH_BOTTOM_MARGIN))
        lineTo(getGraphPositionX(dots.first().x), height * (1 - GRAPH_BOTTOM_MARGIN))
        close()
    }

    private fun getGraphPositionX(x: Double): Float {
        return (x - dots[0].x).toFloat() * gridWidth + graphOffsetX
    }
    private fun getGraphPositionY(y: Double): Float {
        return graphHeight - (y - dots.min().y).toFloat() * gridHeight + graphOffsetY
    }

    fun provideValues(vararg values: Pair<Double, Double>) {
        provideValues(listOf(*values))
    }

    fun provideValues(values: List<Pair<Double, Double>>) {
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

        private const val LABEL_X_DEFAULT_AMOUNT = 10
        private const val LABEL_Y_DEFAULT_AMOUNT = 5
    }
}