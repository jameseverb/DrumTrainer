package com.example.drumtrainer

import android.app.Application
import com.example.drumtrainer.data.local.AppDatabase
import com.example.drumtrainer.data.repository.TrainingRepository

/**
 * 轻量手动依赖注入容器。
 * 项目当前规模不需要引入 Hilt；后续若扩展，可平滑替换为 Hilt。
 */
class AppContainer(application: Application) {

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application)
    }

    val trainingRepository: TrainingRepository by lazy {
        TrainingRepository(
            templateDao = database.templateDao(),
            projectDao = database.projectDao(),
            sessionDao = database.sessionDao(),
        )
    }
}

/** 需在 AndroidManifest.xml 的 <application android:name=".DrumTrainerApp"> 中注册 */
class DrumTrainerApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
