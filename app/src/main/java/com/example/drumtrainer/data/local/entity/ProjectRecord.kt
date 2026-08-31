package com.example.drumtrainer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 某个训练项目在【某一次训练】中的明细：耗时 + 记录的 BPM。
 * 与 [TrainingSession] 是多对一关系。
 */
@Entity(
    tableName = "project_records",
    foreignKeys = [
        ForeignKey(
            entity = TrainingSession::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE, // 删除训练记录时级联删除项目明细
        )
    ],
    indices = [Index("sessionId")],
)
data class ProjectRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** 所属训练记录 id */
    val sessionId: Long,
    /** 快照：项目标题 */
    val projectTitle: String,
    /** 快照：练习内容 / 字谱 */
    val content: String,
    /** 该项目本次耗时（毫秒） */
    val durationMs: Long,
    /** 记录的 BPM；仅当项目 needsBpm=true 时才有值 */
    val bpm: Int? = null,
)
