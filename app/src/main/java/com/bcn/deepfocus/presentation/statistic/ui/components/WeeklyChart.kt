package com.bcn.deepfocus.presentation.statistic.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bcn.deepfocus.presentation.statistic.model.AxisScale
import com.bcn.deepfocus.presentation.statistic.model.ChartPoint
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

@Composable
fun WeeklyChart(
    modifier: Modifier = Modifier,
    chartData: List<ChartPoint>
) {
    if (chartData.isEmpty()) return

    val max = chartData.maxOf { it.value }.coerceAtLeast(1f)
    //interpolate value by time so that canvas can draw single frame when value change (composition phase)
    val animatedChartData = chartData.map { point ->
        val animatedValue by animateFloatAsState(
            targetValue = point.value,
            animationSpec = tween(durationMillis = 650),
            label = "weeklyChartValue"
        )

        point.copy(value = animatedValue)
    }
    val scale = calculateAxisScale(max)
    val textStyle = TextStyle(
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    val textMeasurer = rememberTextMeasurer()

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .width(24.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(top = 12.dp)
            ) {
                val xLabelSpace = 26.dp.toPx()
                val chartHeight = size.height - xLabelSpace
                //draw y label
                scale.labels.forEach { value ->

                    val y = chartHeight - value / scale.maxAxis * chartHeight

                    drawText(
                        textMeasurer = textMeasurer,
                        text = formatMinutes(value),
                        style = textStyle,
                        topLeft = Offset(20f, y)
                    )
                }
            }

        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = 12.dp, end = 10.dp)
        ) {
            // y position of label
            val xLabelSpace = 26.dp.toPx()
            val chartHeight = size.height - xLabelSpace

            // decide the x position of each point in the chart (size.width is the width of canvas)
            val spacing = if (animatedChartData.size == 1) 0f else size.width / (animatedChartData.size - 1)

            //map from data to coordinates
            val points = animatedChartData.mapIndexed { index, point ->
                Offset(
                    // (i = 0 => x = 0, i = 1 => x = 100, i = 2 => x = 200,...)
                    x = if (animatedChartData.size == 1) size.width / 2f else spacing * index,
                    y = chartHeight - (point.value / max) * chartHeight
                )
            }

            val stroke = Path()

            //move to the first point of the list
            stroke.moveTo(
                points.first().x,
                points.first().y
            )

            for (i in 0 until points.lastIndex) {
                val p1 = points[i]
                val p2 = points[i + 1]

                //cx = control point which is the midpoint of line segment p1, p2
                //we need cx to pull up or down to create a curve (it's just like pts)
                val cx = (p1.x + p2.x) / 2

                //cubicTo help to create a curve like a surf, that why it requires 2 point
                stroke.cubicTo(
                    cx,
                    p1.y,

                    cx,
                    p2.y,

                    p2.x,
                    p2.y
                )
            }

            //this path helps fill the bottom line so we can color it
            val fillPath = Path().apply {
                addPath(stroke)

                // pull the stroke to the bottom Canvas
                lineTo(points.last().x, size.height)

                // move to the left corner
                lineTo(points.first().x, size.height)

                // close path (end draw)
                close()
            }

            //draw the background first
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x55FF5C5C),
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = size.height
                )
            )

            // to decorate the line
            drawPath(
                path = stroke,
                color = Color(0xFFFF5C5C),
                style = Stroke(
                    width = 6f,
                    cap = StrokeCap.Round
                )
            )

            //after all, draw circle for each point
            points.forEach { point ->
                drawCircle(
                    color = Color(0xFFFF5C5C),
                    radius = 4.dp.toPx(),
                    center = point
                )
            }

            //draw x label
            animatedChartData.forEachIndexed { index, item ->

                val point = points[index]

                //use measure to get the size (in pixel) of a text
                val result = textMeasurer.measure(item.label)


                drawText(
                    textMeasurer = textMeasurer,
                    text = item.label,
                    style = textStyle,
                    topLeft = Offset(
                        x = (point.x - result.size.width / 2f).coerceIn(
                            0f,
                            size.width - result.size.width / 1.4f
                        ), //use coerceIn to avoid overflow of text
                        y = chartHeight + 8.dp.toPx()
                    )
                )
            }
        }
    }
}

// algorithm to round number or make it perfect
private fun niceNumber(
    value: Float,
    round: Boolean
): Float {

    val exponent = floor(log10(value.toDouble())).toInt()

    val fraction = value / 10f.pow(exponent)

    val niceFraction = if (round) {
        when {
            fraction < 1.5f -> 1f
            fraction < 3f -> 2f
            fraction < 7f -> 5f
            else -> 10f
        }
    } else {
        when {
            fraction <= 1f -> 1f
            fraction <= 2f -> 2f
            fraction <= 5f -> 5f
            else -> 10f
        }
    }

    return niceFraction * 10f.pow(exponent)
}

private fun calculateAxisScale(
    maxValue: Float,
    labelCount: Int = 5
): AxisScale {

    if (maxValue <= 0f) {
        return AxisScale(
            maxAxis = 1f,
            step = 1f,
            labels = listOf(0f, 1f)
        )
    }

    val range = niceNumber(maxValue, round = false)

    val step = niceNumber(
        range / (labelCount - 1),
        round = true
    )

    val maxAxis =
        ceil(maxValue / step) * step

    val labels = mutableListOf<Float>()

    var current = 0f

    while (current <= maxAxis + 0.001f) {
        labels += current
        current += step
    }

    return AxisScale(
        maxAxis = maxAxis,
        step = step,
        labels = labels
    )
}

private fun formatMinutes(value: Float): String {

    val hour = value.toInt() / 60

    val minute = value.toInt() % 60

    return when {
        hour == 0 && minute == 0 -> "0"
        hour == 0 -> "${minute}m"
        minute == 0 -> "${hour}h"
        else -> "${hour}h"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeeklyChart() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        val weeklyData = listOf(
            ChartPoint("Mon", 120f),
            ChartPoint("Tue", 70f),
            ChartPoint("Wed", 150f),
            ChartPoint("Thu", 180f),
            ChartPoint("Fri", 130f),
            ChartPoint("Sat", 220f),
            ChartPoint("Sun", 200f)
        )
        WeeklyChart(
            chartData = weeklyData,
            modifier = Modifier.border(1.dp, Color.Black)
        )
    }
}
