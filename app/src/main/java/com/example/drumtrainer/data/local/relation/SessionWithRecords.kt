package com.example.drumtrainer.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.drumtrainer.data.local.entity.ProjectRecord
import com.example.drumtrainer.data.local.entity.TrainingSession

/**
 * 「一次训练 + 其项目明细」的聚合结果，历史页展开用。
 */
data class SessionWithRecords(
    @Embedded
    val session: TrainingSession,
    @Relation(parentColumn = "id", entityColumn = "sessionId")
    val records: List<ProjectRecord>,
)
