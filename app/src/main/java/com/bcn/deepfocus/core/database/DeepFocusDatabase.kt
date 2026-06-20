package com.bcn.deepfocus.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bcn.deepfocus.model.custom.data.local.dao.FocusSetDao
import com.bcn.deepfocus.model.custom.data.local.entities.FocusSetEntity

@Database(
    entities = [FocusSetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DeepFocusDatabase: RoomDatabase() {
    abstract fun focusDao(): FocusSetDao
}