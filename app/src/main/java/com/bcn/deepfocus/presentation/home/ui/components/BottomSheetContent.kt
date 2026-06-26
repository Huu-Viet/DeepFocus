package com.bcn.deepfocus.presentation.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bcn.deepfocus.R
import com.bcn.deepfocus.presentation.home.model.EFocusPreset

@Composable
fun BottomSheetContent(
    currentFocusSet: EFocusPreset,
    changeSetDuration: (EFocusPreset) -> Unit,
    onContentSelected: () -> Unit,
    onCustomTime:() -> Unit,
) {
    val borderColor = MaterialTheme.colorScheme.primary

    EFocusPreset.entries.forEach { preset ->
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable() {
                    changeSetDuration(preset)
                    onContentSelected()
                }
                .padding(vertical = 12.dp)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .background(color = MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text =
                        when (preset) {
                            EFocusPreset.DEFAULT -> "Pomodoro"
                            EFocusPreset.LONG -> stringResource(R.string.long_focus)
                            else -> stringResource(R.string.deep_focus)
                        },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${preset.focusMinutes} min focus ∙ ${preset.breakMinutes} min break",
                    fontSize = 16.sp
                )
            }

            RadioButton(
                selected = currentFocusSet == preset,
                onClick = {
                    changeSetDuration(preset)
                    onContentSelected()
                }
            )
        }
    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .drawWithContent {
                drawContent()

                val dashEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(20f, 10f),
                    phase = 0f
                )

                drawRoundRect(
                    color = borderColor,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = dashEffect
                    ),
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
            }
            .clickable {
                onContentSelected()
                onCustomTime()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.custom_time),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.set_own_break),
                fontSize = 16.sp
            )
        }

        Icon(
            Icons.AutoMirrored.Default.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.padding(end = 14.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    Column() {
        BottomSheetContent(
            currentFocusSet = EFocusPreset.DEFAULT,
            changeSetDuration = { preset ->

            },
            onContentSelected = {},
            onCustomTime = {}
        )
    }
}