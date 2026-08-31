package com.example.drumtrainer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.drumtrainer.data.local.entity.TrainingProject
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    /** 观察某模版下的全部项目，按 sortOrder 升序（长列表的训练顺序） */
    @Query("SELECT * FROM training_projects WHERE templateId = :templateId ORDER BY sortOrder ASC, id ASC")
    fun observeByTemplate(templateId: Long): Flow<List<TrainingProject>>

    @Insert
    suspend fun insert(project: TrainingProject): Long

    @Update
    suspend fun update(project: TrainingProject)

    @Delete
    suspend fun delete(project: TrainingProject)

    @Query("DELETE FROM training_projects WHERE templateId = :templateId")
    suspend fun deleteByTemplate(templateId: Long)

    /** 编辑模版时一次性替换全部项目（事务内保证原子性） */
    @Transaction
    suspend fun replaceAll(templateId: Long, projects: List<TrainingProject>) {
        deleteByTemplate(templateId)
        projects.forEach { insert(it.copy(templateId = templateId)) }
    }
}
