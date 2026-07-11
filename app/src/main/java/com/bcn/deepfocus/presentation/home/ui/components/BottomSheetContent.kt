package com.bcn.deepfocus.presentation.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bcn.deepfocus.R
import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.presentation.home.model.EFocusPreset

@Composable
fun BottomSheetContent(
    activeFocusSet: FocusSet,
    customFocusSets: List<FocusSet>,
    changeSetDuration: (EFocusPreset) -> Unit,
    onCustomFocusSetSelected: (FocusSet) -> Unit,
    onContentSelected: (FocusSet) -> Unit,
    onCustomTime: () -> Unit,
    onDeleteItem: (id: String) -> Unit,
) {
    val borderColor = MaterialTheme.colorScheme.primary
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        // Preset options
        EFocusPreset.entries.forEach { preset ->
            val isSelected = activeFocusSet.id == preset.name

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        changeSetDuration(preset)
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
                        text = when (preset) {
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
                    selected = isSelected,
                    onClick = {
                        changeSetDuration(preset)
                    }
                )
            }
        }

        // Custom saved focus sets
        if (customFocusSets.isNotEmpty()) {
            Spacer(Modifier.size(8.dp))

            Text(
                text = stringResource(R.string.your_custom_set),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            customFocusSets.forEach { focusSet ->
                val isSelected = activeFocusSet.id == focusSet.id

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCustomFocusSetSelected(focusSet)
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
                            text = stringResource(R.string.custom_time),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${focusSet.focusMinute} min " +
                                    stringResource(R.string.focus_lowercase) +
                                    " ∙ ${focusSet.breakMinute} min " +
                                    stringResource(R.string.break_lowercase),
                            fontSize = 16.sp
                        )
                    }

                    Row {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                onCustomFocusSetSelected(focusSet)
                            }
                        )

                        IconButton(
                            onClick = {onContentSelected(focusSet)}
                        ) {
                            Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
                        }

                        IconButton(
                            onClick = {onDeleteItem(focusSet.id)}
                        ) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.size(8.dp))

        // Custom Time navigation button
        Row(
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

        Spacer(Modifier.size(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    Column {
        BottomSheetContent(
            activeFocusSet = FocusSet(
                id = EFocusPreset.DEFAULT.name,
                focusMinute = 25,
                breakMinute = 5
            ),
            customFocusSets = listOf(
                FocusSet(id = "1", focusMinute = 45, breakMinute = 10),
                FocusSet(id = "2", focusMinute = 60, breakMinute = 15),
            ),
            changeSetDuration = {},
            onCustomFocusSetSelected = {},
            onContentSelected = {a -> },
            onCustomTime = {},
            onDeleteItem = {},
        )
    }
}
