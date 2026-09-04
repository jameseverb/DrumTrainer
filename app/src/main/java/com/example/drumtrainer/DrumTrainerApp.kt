package com.example.drumtrainer

import android.app.Application
import com.example.drumtrainer.R
import com.example.drumtrainer.data.local.AppDatabase
import com.example.drumtrainer.data.local.ThemeSettingsStore
import com.example.drumtrainer.data.local.entity.TrainingProject
import com.example.drumtrainer.data.repository.TrainingRepository
import com.example.drumtrainer.data.seed.DefaultSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 轻量手动依赖注入容器。
 * 项目当前规模不需要引入 Hilt；后续若扩展，可平滑替换为 Hilt。
 */
class AppContainer(private val application: Application) {

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

    val themeSettings: ThemeSettingsStore by lazy {
        ThemeSettingsStore(application)
    }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * 首次启动预置默认训练模板，省去手动输入。
     * 安全策略：按模板名查找，仅当不存在时才插入，绝不覆盖用户已建模板或数据。
     */
    fun seedDefaultDataIfNeeded() {
        appScope.launch {
            val repo = trainingRepository
            val defaultName = application.getString(R.string.default_template_name)
            val existing = repo.observeTemplates().first()
            if (existing.none { it.name == defaultName }) {
                val templateId = repo.addTemplate(defaultName)
                DefaultSeedData.projects.forEach { p ->
                    repo.addProject(
                        TrainingProject(
                            templateId = templateId,
                            title = p.title,
                            content = p.content,
                            needsBpm = p.needsBpm,
                            sortOrder = p.sortOrder,
                        )
                    )
                }
            }
        }
    }
}

/** 需在 AndroidManifest.xml 的 <application android:name=".DrumTrainerApp"> 中注册 */
class DrumTrainerApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        // 预置默认训练模板（首次启动或未存在时），不覆盖已有数据
        container.seedDefaultDataIfNeeded()
    }
}
