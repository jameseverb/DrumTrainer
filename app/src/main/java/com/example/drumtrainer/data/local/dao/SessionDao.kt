package com.example.drumtrainer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.drumtrainer.data.local.entity.ProjectRecord
import com.example.drumtrainer.data.local.entity.TrainingSession
import com.example.drumtrainer.data.local.relation.SessionWithRecords
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: TrainingSession): Long

    @Insert
    suspend fun insertRecord(record: ProjectRecord): Long

    /**
     * 一次性写入「一次训练 + 其全部项目明细」，保证原子性。
     * 返回新训练记录的 id。
     */
    @Transaction
    suspend fun insertSessionWithRecords(
        session: TrainingSession,
        records: List<ProjectRecord>,
    ): Long {
        val sessionId = insertSession(session)
        records.forEach { insertRecord(it.copy(sessionId = sessionId)) }
        return sessionId
    }

    /** 历史页：全部训练记录（最新在前），每条附带项目明细 */
    @Transaction
    @Query("SELECT * FROM training_sessions ORDER BY finishedAt DESC")
    fun observeSessionsWithRecords(): Flow<List<SessionWithRecords>>

    /** 单条训练的明细（备用） */
    @Query("SELECT * FROM project_records WHERE sessionId = :sessionId ORDER BY id ASC")
    fun observeRecords(sessionId: Long): Flow<List<ProjectRecord>>
}
