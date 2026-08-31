package com.example.drumtrainer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 一次完整训练（每点击一次「完成今日训练」生成一条记录）。
 * 与 [ProjectRecord] 是一对多关系。
 */
@Entity(
    tableName = "training_sessions",
    foreignKeys = [
        ForeignKey(
            entity = TrainingTemplate::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.SET_NULL, // 模版被删后历史仍在，templateId 置空
        )
    ],
    indices = [Index("templateId"), Index("finishedAt")],
)
data class TrainingSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** 快照：当时使用的模版 id（模版可能被删除，故同时保存名称快照） */
    val templateId: Long? = null,
    /** 模版名称快照，历史页展示用，不随模版改名而变 */
    val templateName: String,
    /** 整套训练开始时间（epoch millis，真实墙上时间） */
    val startedAt: Long,
    /** 提交（完成）时间（epoch millis） */
    val finishedAt: Long,
    /** 整套训练总耗时（毫秒，真实墙上时间口径） */
    val totalDurationMs: Long,
)
