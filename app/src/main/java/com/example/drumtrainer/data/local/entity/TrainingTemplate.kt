package com.example.drumtrainer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 训练模版：一套完整的训练计划，内含多个 [TrainingProject]。
 * 例如「日常训练」「周末加练」。
 */
@Entity(tableName = "training_templates")
data class TrainingTemplate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** 模版名称，如「日常训练」 */
    val name: String,
    /** 创建时间（epoch millis） */
    val createdAt: Long = System.currentTimeMillis(),
    /** 最后修改时间（epoch millis），模版列表按此倒序展示 */
    val updatedAt: Long = System.currentTimeMillis(),
)
