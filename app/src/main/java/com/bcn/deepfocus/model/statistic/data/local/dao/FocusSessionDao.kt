package com.bcn.deepfocus.model.statistic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bcn.deepfocus.model.statistic.data.local.entities.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(focusSession: FocusSessionEntity): Long

    @Update
    suspend fun updateSession(focusSession: FocusSessionEntity)

    @Query("SELECT * FROM focus_session WHERE id = :sessionId")
    suspend fun getSession(sessionId: Long): FocusSessionEntity?

    @Query("SELECT * FROM focus_session WHERE id = :sessionId")
    fun observeSession(sessionId: Long): Flow<FocusSessionEntity?>

    @Query("""
        SELECT *
        FROM focus_session
        WHERE localDate = :localDate
            AND endAtMillis IS NOT NULL
        ORDER BY startAtMillis DESC
    """)
    fun observeCompletedSessionsByDate(localDate: String): Flow<List<FocusSessionEntity>>

    @Query("""
        SELECT *
        FROM focus_session
        WHERE localDate BETWEEN :startDate AND :endDate
            AND endAtMillis IS NOT NULL
        ORDER BY startAtMillis ASC
    """)
    fun observeCompletedSessionsBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<List<FocusSessionEntity>>

    @Query("""
        SELECT COALESCE(SUM(totalFocusSeconds), 0)
        FROM focus_session
        WHERE localDate = :localDate
            AND endAtMillis IS NOT NULL
    """)
    fun observeTotalFocusSecondsByDate(localDate: String): Flow<Int>

    @Query("""
        SELECT *
        FROM focus_session
        WHERE localDate = :localDate
            AND endAtMillis IS NULL
        ORDER BY startAtMillis DESC
    """)
    fun observeAbandonedSessionsByDate(localDate: String): Flow<List<FocusSessionEntity>>

    @Query("""
        SELECT *
        FROM focus_session
        WHERE localDate BETWEEN :startDate AND :endDate
            AND endAtMillis IS NULL
        ORDER BY startAtMillis ASC
    """)
    fun observeAbandonedSessionsBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<List<FocusSessionEntity>>

    @Query("""
        DELETE FROM focus_session
        WHERE id = :sessionId
    """)
    suspend fun deleteSession(sessionId: Long)
}
