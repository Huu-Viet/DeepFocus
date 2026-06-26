package com.bcn.deepfocus.presentation.custom.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bcn.deepfocus.core.ui.theme.DeepFocusTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MinuteSelector(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    minValue: Int = 1,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val pageCount = maxValue - minValue + 1
    val pagerState = rememberPagerState(
        initialPage = value - minValue,
        pageCount = { pageCount }
    )

    // Sync pager scroll -> value change
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { page ->
                val newValue = page + minValue
                if (newValue != value) {
                    onValueChange(newValue)
                }
            }
    }

    // Sync external value change (buttons) -> pager
    LaunchedEffect(value) {
        val targetPage = value - minValue
        if (pagerState.settledPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.size(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Decrement button
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    ),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.size(16.dp))

            // Number + "min" label
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Horizontal scroll picker (number only)
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.width(100.dp),
                        pageSize = PageSize.Fill
                    ) { page ->
                        Text(
                            text = "${page + minValue}",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    val pageOffset = (
                                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                    ).absoluteValue

                                    alpha = 1f - (pageOffset * 0.6f).coerceIn(0f, 0.8f)
                                    scaleX = 1f - (pageOffset * 0.25f).coerceIn(0f, 0.5f)
                                    scaleY = scaleX
                                }
                        )
                    }
                }

                // Static "min" label
                Text(
                    text = "min",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(Modifier.size(16.dp))

            // Increment button
            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    ),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MinuteSelectorPreview() {
    DeepFocusTheme {
        MinuteSelector(
            label = "Focus Time",
            value = 25,
            maxValue = 180,
            onValueChange = {},
            onIncrement = {},
            onDecrement = {}
        )
    }
}
