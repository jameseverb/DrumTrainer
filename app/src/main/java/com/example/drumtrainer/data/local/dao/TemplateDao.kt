package com.example.drumtrainer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.drumtrainer.data.local.entity.TrainingTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {

    /** 观察全部模版（最近修改的在前），模版列表页使用 */
    @Query("SELECT * FROM training_templates ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<TrainingTemplate>>

    /** 观察单个模版 */
    @Query("SELECT * FROM training_templates WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<TrainingTemplate?>

    @Insert
    suspend fun insert(template: TrainingTemplate): Long

    @Update
    suspend fun update(template: TrainingTemplate)

    @Delete
    suspend fun delete(template: TrainingTemplate)
}
