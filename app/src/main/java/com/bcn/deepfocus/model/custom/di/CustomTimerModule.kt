package com.bcn.deepfocus.model.custom.di

import com.bcn.deepfocus.core.common.ILocalMapper
import com.bcn.deepfocus.core.database.DeepFocusDatabase
import com.bcn.deepfocus.model.custom.data.local.dao.FocusSetDao
import com.bcn.deepfocus.model.custom.data.local.entities.FocusSetEntity
import com.bcn.deepfocus.model.custom.data.mapper_impl.LocalFocusSetMapper
import com.bcn.deepfocus.model.custom.data.repo_impl.CustomTimerRepoImpl
import com.bcn.deepfocus.model.custom.domain.entities.FocusSet
import com.bcn.deepfocus.model.custom.domain.repo.CustomTimerRepo
import com.bcn.deepfocus.model.custom.domain.usecase.DeleteFocusSetUseCase
import com.bcn.deepfocus.model.custom.domain.usecase.GetFocusSetUseCase
import com.bcn.deepfocus.model.custom.domain.usecase.SaveFocusSetUseCase
import com.bcn.deepfocus.model.custom.domain.usecase.UpdateFocusSetUseCase
import com.bcn.deepfocus.model.custom.domain.usecase.WatchAllFocusSetsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CustomTimerModule {
    //DAO
    @Provides
    fun provideFocusSetDao(
        database: DeepFocusDatabase
    ): FocusSetDao = database.focusDao()

    //Mapper
    @Provides
    fun provideLocalFocusSetMapper(): ILocalMapper<FocusSet, FocusSetEntity> = LocalFocusSetMapper()

    //Repo
    @Provides
    @Singleton
    fun provideCustomTimerRepo(
        focusSetDao: FocusSetDao,
        localMapper: ILocalMapper<FocusSet, FocusSetEntity>
    ): CustomTimerRepo = CustomTimerRepoImpl(focusSetDao, localMapper)

    //UseCases
    @Provides
    fun provideSaveFocusSetUseCase(
        customTimerRepo: CustomTimerRepo
    ) = SaveFocusSetUseCase(customTimerRepo)

    @Provides
    fun provideGetFocusSetUseCase(
        customTimerRepo: CustomTimerRepo
    ) = GetFocusSetUseCase(customTimerRepo)

    @Provides
    fun provideUpdateFocusSetUseCase(
        customTimerRepo: CustomTimerRepo
    ) = UpdateFocusSetUseCase(customTimerRepo)

    @Provides
    fun provideDeleteFocusSetUseCase(
        customTimerRepo: CustomTimerRepo
    ) = DeleteFocusSetUseCase(customTimerRepo)

    @Provides
    fun provideWatchAllFocusSetsUseCase(
        customTimerRepo: CustomTimerRepo
    ) = WatchAllFocusSetsUseCase(customTimerRepo)
}
