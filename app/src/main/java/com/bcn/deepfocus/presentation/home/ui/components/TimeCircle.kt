package com.bcn.deepfocus.presentation.home.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bcn.deepfocus.core.ui.theme.DeepFocusTheme

@Composable
fun TimeCircle(
    progress: Float,
    remainTime: String,
    setDuration: String,
    modifier: Modifier = Modifier
) {
    val progressColor = MaterialTheme.colorScheme.onPrimary
    val timeContainerColor = MaterialTheme.colorScheme.primary
    Box(
        contentAlignment = Alignment.Center,
    ) {
        //outline
        Canvas (
            modifier = modifier.size(240.dp)
        ) {
            drawArc(
                color = Color(0xFFDCDCDC),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = 16.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(
                    width = 16.dp.toPx(),
                    cap = StrokeCap.Round

                )
            )
        }

        //inline
        Canvas (
            modifier = modifier
        ) {
            drawArc(
                color = timeContainerColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = 200.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = remainTime,
                fontSize = 62.sp
            )

            Text(
                text = setDuration,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeCirclePreview() {
    DeepFocusTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeCircle(
                progress = 0.2f,
                remainTime = "20:30",
                setDuration = "25m focus / 5m break"
            )
        }
    }
}