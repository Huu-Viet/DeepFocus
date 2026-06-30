package com.bcn.deepfocus.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bcn.deepfocus.model.custom.data.local.dao.FocusSetDao
import com.bcn.deepfocus.model.custom.data.local.entities.FocusSetEntity
import com.bcn.deepfocus.model.statistic.data.local.dao.FocusSessionDao
import com.bcn.deepfocus.model.statistic.data.local.entities.FocusSessionEntity

@Database(
    entities = [
        FocusSetEntity::class,
        FocusSessionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class DeepFocusDatabase: RoomDatabase() {
    abstract fun focusDao(): FocusSetDao
    abstract fun focusSessionDao(): FocusSessionDao
}
