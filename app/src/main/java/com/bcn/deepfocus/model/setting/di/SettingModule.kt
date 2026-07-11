package com.bcn.deepfocus.model.setting.di

import android.content.Context
import com.bcn.deepfocus.model.setting.data.repo_impl.SettingRepositoryImpl
import com.bcn.deepfocus.model.setting.domain.repo.SettingRepository
import com.bcn.deepfocus.model.setting.domain.usecase.ObserveLanguageModeUseCase
import com.bcn.deepfocus.model.setting.domain.usecase.ObserveThemeModeUseCase
import com.bcn.deepfocus.model.setting.domain.usecase.UpdateLanguageModeUseCase
import com.bcn.deepfocus.model.setting.domain.usecase.UpdateThemeModeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingModule {

    @Provides
    @Singleton
    fun provideSettingRepository(
        @ApplicationContext context: Context
    ): SettingRepository = SettingRepositoryImpl(context)

    @Provides
    fun provideObserveThemeModeUseCase(
        repository: SettingRepository
    ) = ObserveThemeModeUseCase(repository)

    @Provides
    fun provideObserveLanguageModeUseCase(
        repository: SettingRepository
    ) = ObserveLanguageModeUseCase(repository)

    @Provides
    fun provideUpdateThemeModeUseCase(
        repository: SettingRepository
    ) = UpdateThemeModeUseCase(repository)

    @Provides
    fun provideUpdateLanguageModeUseCase(
        repository: SettingRepository
    ) = UpdateLanguageModeUseCase(repository)
}
