package com.example.drumtrainer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun templateDao(): TemplateDao
    abstract fun projectDao(): ProjectDao
    abstract fun sessionDao(): SessionDao

    companion object {
        /** v1 → v2：project_records 新增 projectId 列（可空，存量行为 NULL） */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE project_records ADD COLUMN projectId INTEGER")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "drum_trainer.db",
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
