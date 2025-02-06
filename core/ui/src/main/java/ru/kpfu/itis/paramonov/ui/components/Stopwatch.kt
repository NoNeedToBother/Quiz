package ru.kpfu.itis.paramonov.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Stopwatch(
    time: Int,
    modifier: Modifier = Modifier,
    strokeColor: Color = Color.Black,
    fillColor: Color = Color.LightGray,
    clockSize: Dp = 60.dp
) {
    BoxWithConstraints(
        modifier = modifier
            .size(clockSize)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val size = maxWidth.toPx()
            val center = size / 2f
            val strokeWidth = size.times(0.05f)
            val radius = (size - strokeWidth) / 2f
            val angle = (Math.PI * (time % 60) / 30).toFloat()
            val currentX = center + center * sin(angle)
            val currentY = center * (1 - cos(angle))

            val path = Path().apply {
                moveTo(center, center)
                lineTo(center, 0f)
                lineTo(size, 0f)
                if (angle > Math.PI / 2) lineTo(size, size)
                if (angle > Math.PI) lineTo(0f, size)
                if (angle > 3 * Math.PI / 2) lineTo(0f, 0f)
                lineTo(currentX, currentY)
                close()
            }
            clipPath(path) {
                drawCircle(center = Offset(center, center), radius = radius, color = fillColor)
            }

            drawCircle(center = Offset(center, center),
                radius = radius, color = strokeColor,
                style = Stroke(strokeWidth)
            )

            val markRadiusPortion = 0.2f
            drawLine(strokeColor, Offset(center, 0f), Offset(center, size * markRadiusPortion), strokeWidth)
            drawLine(strokeColor, Offset(size, center), Offset(size * (1 - markRadiusPortion), center), strokeWidth)
            drawLine(strokeColor, Offset(center, size), Offset(center, size * (1 - markRadiusPortion)), strokeWidth)
            drawLine(strokeColor, Offset(0f, center), Offset(size * markRadiusPortion, center), strokeWidth)

            drawLine(strokeColor, Offset(center, center), Offset(currentX, currentY), strokeWidth)
        }
    }
}
