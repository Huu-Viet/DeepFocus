package com.bcn.deepfocus.presentation.statistic.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bcn.deepfocus.model.statistic.domain.entities.DailyFocusSummary
import com.bcn.deepfocus.model.statistic.domain.usecase.WatchDailyFocusSummariesUseCase
import com.bcn.deepfocus.presentation.statistic.model.ChartPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val watchDailyFocusSummariesUseCase: WatchDailyFocusSummariesUseCase,
): ViewModel() {
    //Default date
    private val today = LocalDate.now()
    private val yesterday = today.minusDays(1)
    private val startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    private val _focusChartPoints = MutableStateFlow(emptyList<DailyFocusSummary>().toWeeklyFocusChartPoints(startDate))
    val focusChartPoints = _focusChartPoints.asStateFlow()
    private val _breakChartPoints = MutableStateFlow(emptyList<DailyFocusSummary>().toWeeklyBreakChartPoints(startDate))
    val breakChartPoints = _breakChartPoints.asStateFlow()

    private val _dataForDailySummary = MutableStateFlow(emptyMap<String, DailyFocusSummary?>())
    val dataForDailySummary = _dataForDailySummary.asStateFlow()

    init {
        watchDailyFocusInWeek(startDate, startDate.plusDays(6))
    }

    private fun watchDailyFocusInWeek(
        startDate: LocalDate,
        endDate: LocalDate,
    ) = viewModelScope.launch {
        watchDailyFocusSummariesUseCase(startDate.toString(), endDate.toString()).collect { summaries ->
            _focusChartPoints.update { summaries.toWeeklyFocusChartPoints(startDate) }
            _breakChartPoints.update { summaries.toWeeklyBreakChartPoints(startDate) }
            _dataForDailySummary.update { summaries.getTodayAndYesterdayData() }
        }
    }

    private fun List<DailyFocusSummary>.toWeeklyFocusChartPoints(
        weekStartDate: LocalDate
    ): List<ChartPoint> {
        //Turn List<DailyFocusSummary> -> Map<String, DailyFocusSummary> with String is localDate(key)
        val summaryByDate = associateBy { it.localDate }

        return (0L..6L).map { dayOffset ->
            //Get the date which is equivalent with the offset
            val date = weekStartDate.plusDays(dayOffset)
            //Get the data from the key
            val summary = summaryByDate[date.toString()]

            ChartPoint(
                label = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                value = summary?.totalFocusSeconds?.secondsToMinutesFloat() ?: 0f
            )
        }
    }

    private fun List<DailyFocusSummary>.getTodayAndYesterdayData(): Map<String, DailyFocusSummary?> {
        val summaryByDate = associateBy { it.localDate }

        return mapOf(
            today.toString() to summaryByDate[today.toString()],
            yesterday.toString() to summaryByDate[yesterday.toString()]
        )
    }

    private fun List<DailyFocusSummary>.toWeeklyBreakChartPoints(
        weekStartDate: LocalDate
    ): List<ChartPoint> {
        val summaryByDate = associateBy { it.localDate }

        return (0L..6L).map { dayOffset ->
            val date = weekStartDate.plusDays(dayOffset)
            val summary = summaryByDate[date.toString()]

            ChartPoint(
                label = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                value = summary?.totalBreakSeconds?.secondsToMinutesFloat() ?: 0f
            )
        }
    }

    private fun Int.secondsToMinutesFloat(): Float = this / 60f
}
