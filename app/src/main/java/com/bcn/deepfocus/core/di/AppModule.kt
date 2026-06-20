package com.bcn.deepfocus.core.di

import android.content.Context
import androidx.room.Room
import com.bcn.deepfocus.core.database.DeepFocusDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DeepFocusDatabase = Room.databaseBuilder(
        context,
        DeepFocusDatabase::class.java,
        "deepfocus_database"
    )
        .fallbackToDestructiveMigration(true)
        .build()
}