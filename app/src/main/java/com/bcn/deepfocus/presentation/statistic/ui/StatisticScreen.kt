package com.bcn.deepfocus.presentation.statistic.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bcn.deepfocus.R
import com.bcn.deepfocus.core.ui.theme.DeepFocusTheme
import com.bcn.deepfocus.presentation.statistic.ui.components.DailyInfo
import com.bcn.deepfocus.presentation.statistic.ui.components.WeeklyChart
import com.bcn.deepfocus.presentation.statistic.viewmodel.StatisticViewModel
import java.time.LocalDate

private const val TAG = "StatisticScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticScreen(
    modifier: Modifier = Modifier,
    statisticVM: StatisticViewModel = hiltViewModel()
) {
    val focusChartPoint by statisticVM.focusChartPoints.collectAsStateWithLifecycle()
    val breakChartPoint by statisticVM.breakChartPoints.collectAsStateWithLifecycle()
    val dailySummary by statisticVM.dataForDailySummary.collectAsStateWithLifecycle()

    val today = LocalDate.now()
    val scrollState = rememberScrollState()

    DeepFocusTheme{
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.statistic),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 8.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(enabled = true, state = scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    WeeklyChart(
                        chartData = focusChartPoint
                    )

                    DailyInfo(
                        previousTotalFocus = dailySummary[today.minusDays(1).toString()]
                            ?.totalFocusSeconds ?: 0,
                        dailyFocusSummary = dailySummary[today.toString()]
                    )
                }
            }
        }
    }
}