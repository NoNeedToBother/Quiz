package ru.kpfu.itis.paramonov.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

private const val GRAPH_MARGIN = 0.1f
private const val GRAPH_BOTTOM_MARGIN = 0.2f

@Composable
fun Graph(
    modifier: Modifier = Modifier,
    values: List<Pair<Double, Double>>,
    dotSize: Float = 6f,
    dotColor: Color = Color.Black,
    graphStrokeColor: Color = Color.Black,
    graphStrokeWidth: Float = 4f,
    graphFillColor: Color = Color.Gray,
    graphGradientColor: Color = Color.White,
    gradient: Boolean = false,
    labelXAmount: Int = 10,
    labelYAmount: Int = 5
) {
    val sortedValues = values.sortedBy { it.first }
    if (sortedValues.isEmpty()) return

    val xMin = sortedValues.first().first
    val xMax = sortedValues.last().first
    val yMin = sortedValues.minOf { it.second }
    val yMax = sortedValues.maxOf { it.second }

    val xInterval = (xMax - xMin).takeIf { it != 0.0 } ?: 1.0
    val yInterval = (yMax - yMin).takeIf { it != 0.0 } ?: 1.0

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val canvasHeight = (screenWidth * 9 / 16f)

    Canvas(modifier = modifier.padding(8.dp).height(canvasHeight).width(screenWidth)) {
        val width = size.width
        val height = size.height
        val graphOffsetX = width * GRAPH_MARGIN
        val graphOffsetY = height * GRAPH_MARGIN
        val graphWidth = width * (1 - 2 * GRAPH_MARGIN)
        val graphHeight = canvasHeight.value * (1 - GRAPH_MARGIN - GRAPH_BOTTOM_MARGIN)

        fun mapX(x: Double) = ((x - xMin) / xInterval * graphWidth + graphOffsetX).toFloat()
        fun mapY(y: Double) = (graphHeight - (y - yMin) / yInterval * graphHeight + graphOffsetY).toFloat()

        val graphPath = Path().apply {
            sortedValues.forEachIndexed { index, (x, y) ->
                val posX = mapX(x)
                val posY = mapY(y)
                if (index == 0) moveTo(posX, posY) else lineTo(posX, posY)
            }
        }

        val path = Path().apply {
            sortedValues.forEachIndexed { index, (x, y) ->
                val posX = mapX(x)
                val posY = mapY(y)
                if (index == 0) moveTo(posX, posY) else lineTo(posX, posY)
            }
            lineTo(mapX(xMax), height * 0.8f)
            lineTo(mapX(xMin), height * 0.8f)
            close()
        }

        if (gradient) {
            drawPath(path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(graphFillColor, graphGradientColor),
                    startY = 0f, endY = height
                ),
                style = Fill
            )
        } else { drawPath(path, color = graphFillColor) }

        drawPath(graphPath, color = graphStrokeColor, style = Stroke(width = graphStrokeWidth))

        sortedValues.forEach { (x, y) ->
            drawCircle(color = dotColor, radius = dotSize, center = Offset(mapX(x), mapY(y)))
        }

        for (i in 0 until labelXAmount) {
            val labelValue = xMin + i * xInterval / (labelXAmount - 1)
            val posX = mapX(labelValue)
            drawContext.canvas.nativeCanvas.drawText(
                "%.2f".format(labelValue),
                posX,
                height * 0.85f,
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 20f
                }
            )
        }

        for (i in 0 until labelYAmount) {
            val labelValue = yMin + i * yInterval / (labelYAmount - 1)
            val posY = mapY(labelValue)
            drawContext.canvas.nativeCanvas.drawText(
                "%.2f".format(labelValue),
                graphOffsetX * 0.5f,
                posY,
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 20f
                }
            )
        }
    }
}
