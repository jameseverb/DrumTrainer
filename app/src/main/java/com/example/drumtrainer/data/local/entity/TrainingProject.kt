package com.example.drumtrainer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 训练项目：某个模版下的一个练习项。
 * 例如「阶段一：热身与左手唤醒」。
 */
@Entity(
    tableName = "training_projects",
    foreignKeys = [
        ForeignKey(
            entity = TrainingTemplate::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.CASCADE, // 删除模版时级联删除其项目
        )
    ],
    indices = [Index("templateId")],
)
data class TrainingProject(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** 所属模版 id */
    val templateId: Long,
    /** 项目标题，如「阶段一：热身与左手唤醒」 */
    val title: String,
    /** 练习内容 / 字谱，多行文本，如「LRLR LRLR 从 85 BPM 开始……」 */
    val content: String,
    /** 是否需要记录 BPM */
    val needsBpm: Boolean = false,
    /** 排序权重，越小越靠前 */
    val sortOrder: Int = 0,
)
