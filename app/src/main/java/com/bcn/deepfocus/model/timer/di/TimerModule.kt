package com.bcn.deepfocus.model.timer.di

import com.bcn.deepfocus.model.timer.data.repo_impl.TimerRepoImpl
import com.bcn.deepfocus.model.timer.domain.repo.TimerRepository
import com.bcn.deepfocus.model.timer.domain.usecase.RestartTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.StartTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.StopTimerUseCase
import com.bcn.deepfocus.model.timer.domain.usecase.WatchTimerUseCase
import com.bcn.deepfocus.presentation.service.TimerServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {
    @Provides
    @Singleton
    fun provideTimerRepo(
        timerServiceManager: TimerServiceManager
    ): TimerRepository {
        return TimerRepoImpl(timerServiceManager)
    }

    @Provides
    fun provideStartTimerUseCase(
        timerRepo: TimerRepository
    ) = StartTimerUseCase(timerRepo)

    @Provides
    fun provideStopTimerUseCase(
        timerRepo: TimerRepository
    ) = StopTimerUseCase(timerRepo)

    @Provides
    fun provideRestartTimerUseCase(
        timerRepo: TimerRepository
    ) = RestartTimerUseCase(timerRepo)

    @Provides
    fun provideWatchTimerUseCase(
        timerRepo: TimerRepository
    ) = WatchTimerUseCase(timerRepo)
}