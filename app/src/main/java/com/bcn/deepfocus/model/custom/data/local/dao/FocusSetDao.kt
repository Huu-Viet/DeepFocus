package com.bcn.deepfocus.model.custom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bcn.deepfocus.model.custom.data.local.entities.FocusSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSet(focusSetEntity: FocusSetEntity)

    @Query("SELECT * FROM focus_set AS f WHERE f.id = :setId")
    fun getFocusSet(setId: String): Flow<FocusSetEntity?>

    @Query("SELECT * FROM focus_set")
    fun getAllFocusSets(): Flow<List<FocusSetEntity>>

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