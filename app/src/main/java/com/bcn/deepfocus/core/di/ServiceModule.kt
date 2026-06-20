package com.bcn.deepfocus.core.di

import android.content.Context
import com.bcn.deepfocus.presentation.service.TimerServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideTimerManager(
        @ApplicationContext context: Context
    ): TimerServiceManager {
        return TimerServiceManager(context)
    }
}