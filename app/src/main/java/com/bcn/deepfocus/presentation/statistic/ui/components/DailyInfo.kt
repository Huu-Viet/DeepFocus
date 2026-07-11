package com.bcn.deepfocus.presentation.statistic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.InsertChart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bcn.deepfocus.R
import com.bcn.deepfocus.model.statistic.domain.entities.DailyFocusSummary
import com.bcn.deepfocus.utils.formatDuration

@Composable
fun DailyInfo(
    modifier: Modifier = Modifier,
    previousTotalFocus: Int,
    dailyFocusSummary: DailyFocusSummary?
) {
    val focusRatio = dailyFocusSummary?.let {
        (it.totalFocusSeconds / (it.totalFocusSeconds + it.totalBreakSeconds).toFloat()) * 100
    } ?: 0f
    val compareRatio = dailyFocusSummary?.let {
        if (previousTotalFocus > 0) {
            (it.totalFocusSeconds / previousTotalFocus.toFloat()) * 100
        } else -1f
    } ?: 0f

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(12.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    Icons.Rounded.InsertChart,
                    modifier = Modifier.size(16.dp),
                    contentDescription = null
                )
                Spacer(Modifier.size(10.dp))
                Text(stringResource(R.string.today_focus_time))
            }

            if (compareRatio > 100) {
                Text("> ${(compareRatio - 100).let { "%.2f".format(it) }}% ${stringResource(R.string.compare_to_yesterday)}")
            } else if (compareRatio.equals(0f)) {
                Text(stringResource(R.string.no_session_yet))
            } else if (compareRatio.equals(-1f)) {
                Text(stringResource(R.string.no_session_yesterday))
            } else {
                Text("< ${compareRatio.let { "%.2f".format(it) }}% ${stringResource(R.string.compare_to_yesterday)}")
            }

            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)

            Text("${stringResource(R.string.focus_time)}: ${formatDuration(dailyFocusSummary?.totalFocusSeconds ?: 0)} ")

            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)

            Text("${stringResource(R.string.break_time)}: ${formatDuration(dailyFocusSummary?.totalBreakSeconds ?: 0)}")

            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)

            Text("${stringResource(R.string.cycles)}: ${dailyFocusSummary?.completedCycles ?: 0}")

            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)

            Text("${stringResource(R.string.sessions)}: ${dailyFocusSummary?.sessionCount ?: 0}")

            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)

            Text("${stringResource(R.string.focus_efficiency)}: ${"%.2f".format(focusRatio)}%")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDailyInfo() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DailyInfo(
            previousTotalFocus = 2200,
            dailyFocusSummary = null
        )
    }
}