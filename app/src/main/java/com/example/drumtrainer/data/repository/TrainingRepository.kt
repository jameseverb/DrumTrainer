package com.example.drumtrainer.data.repository

import com.example.drumtrainer.data.local.dao.ProjectDao
import com.example.drumtrainer.data.local.dao.SessionDao
import com.example.drumtrainer.data.local.dao.TemplateDao
import com.example.drumtrainer.data.local.entity.ProjectRecord
import com.example.drumtrainer.data.local.entity.TrainingProject
import com.example.drumtrainer.data.local.entity.TrainingSession
import com.example.drumtrainer.data.local.entity.TrainingTemplate
import com.example.drumtrainer.data.local.relation.SessionWithRecords
import kotlinx.coroutines.flow.Flow

/**
 * 仓库层：对 ViewModel 屏蔽具体 DAO 细节。
 * 后续若引入事务、校验、数据同步等逻辑，集中在此处。
 */
class TrainingRepository(
    private val templateDao: TemplateDao,
    private val projectDao: ProjectDao,
    private val sessionDao: SessionDao,
) {

    // ---------- 模版 ----------
    fun observeTemplates(): Flow<List<TrainingTemplate>> = templateDao.observeAll()

    fun observeTemplate(id: Long): Flow<TrainingTemplate?> = templateDao.observeById(id)

    suspend fun addTemplate(name: String): Long =
        templateDao.insert(TrainingTemplate(name = name))

    suspend fun renameTemplate(template: TrainingTemplate, newName: String) =
        templateDao.update(template.copy(name = newName, updatedAt = System.currentTimeMillis()))

    suspend fun deleteTemplate(template: TrainingTemplate) = templateDao.delete(template)

    // ---------- 项目 ----------
    fun observeProjects(templateId: Long): Flow<List<TrainingProject>> =
        projectDao.observeByTemplate(templateId)

    suspend fun addProject(project: TrainingProject): Long = projectDao.insert(project)

    suspend fun updateProject(project: TrainingProject) = projectDao.update(project)

    suspend fun deleteProject(project: TrainingProject) = projectDao.delete(project)

    /** 编辑模版时一次性替换全部项目（原子操作） */
    suspend fun replaceProjects(templateId: Long, projects: List<TrainingProject>) =
        projectDao.replaceAll(templateId, projects)

    // ---------- 历史记录 ----------
    fun observeSessionsWithRecords(): Flow<List<SessionWithRecords>> =
        sessionDao.observeSessionsWithRecords()

    /** 提交一次训练：原子写入 session + 全部项目明细 */
    suspend fun submitSession(session: TrainingSession, records: List<ProjectRecord>): Long =
        sessionDao.insertSessionWithRecords(session, records)
}
