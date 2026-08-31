package com.example.drumtrainer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.drumtrainer.data.local.dao.ProjectDao
import com.example.drumtrainer.data.local.dao.SessionDao
import com.example.drumtrainer.data.local.dao.TemplateDao
import com.example.drumtrainer.data.local.entity.ProjectRecord
import com.example.drumtrainer.data.local.entity.TrainingProject
import com.example.drumtrainer.data.local.entity.TrainingSession
import com.example.drumtrainer.data.local.entity.TrainingTemplate

@Database(
    entities = [
        TrainingTemplate::class,
        TrainingProject::class,
        TrainingSession::class,
        ProjectRecord::class,
    ],
    version = 1,
    // 生产环境建议改为 true，并配置 room.schemaLocation 以支持后续数据库迁移
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun templateDao(): TemplateDao
    abstract fun projectDao(): ProjectDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "drum_trainer.db",
                ).build().also { INSTANCE = it }
            }
    }
}
