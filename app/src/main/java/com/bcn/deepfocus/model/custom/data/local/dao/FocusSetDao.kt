package com.bcn.deepfocus.model.custom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bcn.deepfocus.model.custom.data.local.entities.FocusSetEntity

@Dao
interface FocusSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSet(focusSetEntity: FocusSetEntity)

    @Query("SELECT * FROM focus_set AS f WHERE f.id = :setId")
    suspend fun getFocusSet(setId: String): FocusSetEntity?

    @Query("""
        UPDATE focus_set
        SET focusMinute = :focusMinute, breakMinute = :breakMinute
        WHERE id = :setId
    """)
    suspend fun updateFocusSet(setId: String, focusMinute: Int, breakMinute: Int)

    @Query("""
        DELETE FROM focus_set
        WHERE id = :setId
    """)
    suspend fun deleteFocusSet(setId: String)
}