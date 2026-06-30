package com.bcn.deepfocus.model.statistic.di

import com.bcn.deepfocus.core.common.ILocalMapper
import com.bcn.deepfocus.core.database.DeepFocusDatabase
import com.bcn.deepfocus.model.statistic.data.local.dao.FocusSessionDao
import com.bcn.deepfocus.model.statistic.data.local.entities.FocusSessionEntity
import com.bcn.deepfocus.model.statistic.data.mapper_impl.LocalFocusSessionMapper
import com.bcn.deepfocus.model.statistic.data.repo_impl.StatisticRepositoryImpl
import com.bcn.deepfocus.model.statistic.domain.entities.FocusSession
import com.bcn.deepfocus.model.statistic.domain.repo.StatisticRepository
import com.bcn.deepfocus.model.statistic.domain.usecase.DeleteFocusSessionUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.GetFocusSessionUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.SaveFocusSessionUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.UpdateFocusSessionUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.WatchAbandonedSessionsBetweenDatesUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.WatchAbandonedSessionsByDateUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.WatchCompletedSessionsBetweenDatesUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.WatchCompletedSessionsByDateUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.WatchDailyFocusSummariesUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.WatchFocusSessionUseCase
import com.bcn.deepfocus.model.statistic.domain.usecase.WatchTotalFocusSecondsByDateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatisticModule {
    //DAO
    @Provides
    fun provideFocusSessionDao(
        database: DeepFocusDatabase
    ): FocusSessionDao = database.focusSessionDao()

    //Mapper
    @Provides
    fun provideLocalFocusSessionMapper(): ILocalMapper<FocusSession, FocusSessionEntity> =
        LocalFocusSessionMapper()

    //Repo
    @Provides
    @Singleton
    fun provideStatisticRepository(
        focusSessionDao: FocusSessionDao,
        localMapper: ILocalMapper<FocusSession, FocusSessionEntity>
    ): StatisticRepository = StatisticRepositoryImpl(focusSessionDao, localMapper)

    //UseCase
    @Provides
    fun provideSaveFocusSessionUseCase(
        statisticRepository: StatisticRepository
    ) = SaveFocusSessionUseCase(statisticRepository)

    @Provides
    fun provideUpdateFocusSessionUseCase(
        statisticRepository: StatisticRepository
    ) = UpdateFocusSessionUseCase(statisticRepository)

    @Provides
    fun provideGetFocusSessionUseCase(
        statisticRepository: StatisticRepository
    ) = GetFocusSessionUseCase(statisticRepository)

    @Provides
    fun provideWatchFocusSessionUseCase(
        statisticRepository: StatisticRepository
    ) = WatchFocusSessionUseCase(statisticRepository)

    @Provides
    fun provideWatchCompletedSessionsByDateUseCase(
        statisticRepository: StatisticRepository
    ) = WatchCompletedSessionsByDateUseCase(statisticRepository)

    @Provides
    fun provideWatchCompletedSessionsBetweenDatesUseCase(
        statisticRepository: StatisticRepository
    ) = WatchCompletedSessionsBetweenDatesUseCase(statisticRepository)

    @Provides
    fun provideWatchAbandonedSessionsByDateUseCase(
        statisticRepository: StatisticRepository
    ) = WatchAbandonedSessionsByDateUseCase(statisticRepository)

    @Provides
    fun provideWatchAbandonedSessionsBetweenDatesUseCase(
        statisticRepository: StatisticRepository
    ) = WatchAbandonedSessionsBetweenDatesUseCase(statisticRepository)

    @Provides
    fun provideWatchTotalFocusSecondsByDateUseCase(
        statisticRepository: StatisticRepository
    ) = WatchTotalFocusSecondsByDateUseCase(statisticRepository)

    @Provides
    fun provideWatchDailyFocusSummariesUseCase(
        statisticRepository: StatisticRepository
    ) = WatchDailyFocusSummariesUseCase(statisticRepository)

    @Provides
    fun provideDeleteFocusSessionUseCase(
        statisticRepository: StatisticRepository
    ) = DeleteFocusSessionUseCase(statisticRepository)
}
