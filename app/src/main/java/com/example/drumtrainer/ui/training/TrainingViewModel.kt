package com.example.drumtrainer.ui.training

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.drumtrainer.DrumTrainerApp
import com.example.drumtrainer.core.timer.TimerState
import com.example.drumtrainer.data.local.entity.ProjectRecord
import com.example.drumtrainer.data.local.entity.TrainingProject
import com.example.drumtrainer.data.local.entity.TrainingSession
import com.example.drumtrainer.data.local.entity.TrainingTemplate
import com.example.drumtrainer.data.repository.TrainingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/** 「完成今日训练」按钮的提交状态 */
enum class SubmitState { Idle, Saving, Success, Error }

/**
 * 训练页 UI 状态：一次性聚合所有驱动界面的数据。
 */
data class TrainingUiState(
    val template: TrainingTemplate? = null,
    val projects: List<TrainingProject> = emptyList(),
    /** 整套训练总计时器（原始状态） */
    val totalTimer: TimerState = TimerState(),
    /** 整套训练总耗时（毫秒，已按当前时刻折算） */
    val totalElapsedMs: Long = 0L,
    /** 总计时器首次启动时的真实墙上时间，提交时写入历史记录 */
    val totalStartedAtWallMs: Long? = null,
    /** 各项目计时器原始状态，key = projectId */
    val projectTimers: Map<Long, TimerState> = emptyMap(),
    /** 各项目已折算耗时（毫秒），key = projectId */
    val projectElapsedMs: Map<Long, Long> = emptyMap(),
    /** 当前正在计时的项目 id（互斥：同时仅一个在跑） */
    val runningProjectId: Long? = null,
    /** BPM 输入（字符串，key = projectId；提交时才转 Int?） */
    val bpmInputs: Map<Long, String> = emptyMap(),
    /** 各项目上次训练记录的 BPM（key = projectId），无记录则缺席 */
    val lastBpms: Map<Long, Int> = emptyMap(),
    val submitState: SubmitState = SubmitState.Idle,
)

/**
 * 训练页 ViewModel。
 *
 * 核心职责：
 * 1. 监听当前模版及其项目列表（Room Flow 自动刷新）；
 * 2. 管理「整套总计时器」与「各项目计时器」；
 * 3. 项目计时器互斥（开始新项目自动暂停上一个）；
 * 4. 收集各项目 BPM 输入，点击完成后一次性提交到 Room。
 */
class TrainingViewModel(
    private val repository: TrainingRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    /** 从导航参数读取当前要训练的模版 id */
    private val templateId: Long = checkNotNull(savedStateHandle.get<Long>("templateId")) {
        "缺少导航参数 templateId"
    }

    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    init {
        // 1. 监听模版与其项目列表（Room 数据变化自动刷新）
        viewModelScope.launch {
            combine(
                repository.observeTemplate(templateId),
                repository.observeProjects(templateId),
            ) { template, projects -> template to projects }
                .collect { (template, projects) ->
                    _uiState.update { it.copy(template = template, projects = projects) }
                    // 项目集合变化时（首次进入 / 编辑后返回），加载各项目上次的 BPM
                    maybeLoadLastBpms(projects)
                }
        }

        // 2. 计时刷新循环：有计时器在跑时，每 TICK_MS 折算一次当前耗时
        viewModelScope.launch {
            while (isActive) {
                delay(TICK_MS)
                val s = _uiState.value
                if (s.totalTimer.isRunning || s.projectTimers.values.any { it.isRunning }) {
                    recomputeElapsed()
                }
            }
        }
    }

    // ---------- 整套训练总计时器 ----------

    fun startTotal() {
        val now = SystemClock.elapsedRealtime()
        _uiState.update { s ->
            if (s.totalTimer.isRunning) s
            else s.copy(
                totalTimer = s.totalTimer.start(now),
                // 首次启动时记录真实墙上时间，作为历史记录的 startedAt
                totalStartedAtWallMs = s.totalStartedAtWallMs ?: System.currentTimeMillis(),
            )
        }
        recomputeElapsed()
    }

    fun pauseTotal() {
        val now = SystemClock.elapsedRealtime()
        _uiState.update { it.copy(totalTimer = it.totalTimer.pause(now)) }
        recomputeElapsed()
    }

    fun resetTotal() {
        _uiState.update {
            it.copy(totalTimer = TimerState(), totalElapsedMs = 0L, totalStartedAtWallMs = null)
        }
    }

    // ---------- 项目计时器（互斥） ----------

    /**
     * 开始/暂停某个项目计时器。
     * 互斥规则：开始新项目时，自动暂停上一个正在计时的项目。
     */
    fun toggleProjectTimer(projectId: Long) {
        val now = SystemClock.elapsedRealtime()
        _uiState.update { s ->
            val currentRunning = s.runningProjectId
            when (currentRunning) {
                projectId -> {
                    // 该项目已在跑 → 暂停它
                    s.copy(
                        projectTimers = s.projectTimers + (
                            projectId to (s.projectTimers[projectId] ?: TimerState()).pause(now)
                        ),
                        runningProjectId = null,
                    )
                }

                else -> {
                    var timers = s.projectTimers
                    // 暂停上一个正在跑的项目
                    currentRunning?.let { running ->
                        timers = timers + (running to (timers[running] ?: TimerState()).pause(now))
                    }
                    // 启动当前项目
                    timers = timers + (projectId to (timers[projectId] ?: TimerState()).start(now))
                    s.copy(projectTimers = timers, runningProjectId = projectId)
                }
            }
        }
        recomputeElapsed()
    }

    fun resetProjectTimer(projectId: Long) {
        _uiState.update { s ->
            s.copy(
                projectTimers = s.projectTimers + (projectId to TimerState()),
                runningProjectId = if (s.runningProjectId == projectId) null else s.runningProjectId,
            )
        }
        recomputeElapsed()
    }

    // ---------- BPM 输入 ----------

    fun setBpm(projectId: Long, value: String) {
        // 仅保留数字，最多 4 位（BPM 0~999 足够）
        val digits = value.filter(Char::isDigit).take(4)
        _uiState.update { it.copy(bpmInputs = it.bpmInputs + (projectId to digits)) }
    }

    // ---------- 提交 ----------

    fun submitSession() {
        val s = _uiState.value
        if (s.submitState == SubmitState.Saving) return
        if (s.projects.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(submitState = SubmitState.Saving) }
            try {
                val nowWall = System.currentTimeMillis()
                val nowRealtime = SystemClock.elapsedRealtime()

                // 汇总每个项目的明细（耗时 + BPM）
                val records = s.projects.map { p ->
                    val duration = (s.projectTimers[p.id] ?: TimerState()).elapsedMs(nowRealtime)
                    val bpm = if (p.needsBpm) s.bpmInputs[p.id]?.toIntOrNull() else null
                    ProjectRecord(
                        sessionId = 0L, // 由 DAO 在事务内回填
                        projectTitle = p.title,
                        content = p.content,
                        durationMs = duration,
                        bpm = bpm,
                        projectId = p.id,
                    )
                }

                val session = TrainingSession(
                    templateId = s.template?.id,
                    templateName = s.template?.name ?: "",
                    startedAt = s.totalStartedAtWallMs ?: nowWall,
                    finishedAt = nowWall,
                    totalDurationMs = s.totalTimer.elapsedMs(nowRealtime),
                )

                repository.submitSession(session, records)
                _uiState.update { it.copy(submitState = SubmitState.Success) }
            } catch (e: Exception) {
                _uiState.update { it.copy(submitState = SubmitState.Error) }
            }
        }
    }

    /** UI 展示完结果后，复位提交状态 */
    fun consumeSubmitResult() {
        _uiState.update { it.copy(submitState = SubmitState.Idle) }
    }

    // ---------- 内部 ----------

    /** 上次已加载过 BPM 的项目集合，避免 Flow 重复发射时反复查询数据库 */
    private var loadedBpmProjectIds: Set<Long> = emptySet()

    /**
     * 加载各项目上次的 BPM 并预填到输入框（不覆盖用户已输入的值）。
     * 仅在项目集合发生变化时执行。
     */
    private suspend fun maybeLoadLastBpms(projects: List<TrainingProject>) {
        val currentIds = projects.map { it.id }.toSet()
        if (currentIds == loadedBpmProjectIds) return
        loadedBpmProjectIds = currentIds

        val last = buildMap {
            projects.filter { it.needsBpm }.forEach { p ->
                repository.getLastBpm(p.id)?.let { put(p.id, it) }
            }
        }
        _uiState.update { s ->
            // 预填：仅填空白输入框，不覆盖用户已输入的内容
            val prefilled = last.mapValues { it.value.toString() }
                .filterKeys { it !in s.bpmInputs }
            s.copy(
                lastBpms = last,
                bpmInputs = s.bpmInputs + prefilled,
            )
        }
    }

    /** 把原始 TimerState 折算成当前耗时并写入 state */
    private fun recomputeElapsed() {
        val now = SystemClock.elapsedRealtime()
        _uiState.update { s ->
            s.copy(
                totalElapsedMs = s.totalTimer.elapsedMs(now),
                projectElapsedMs = s.projects.associate { p ->
                    p.id to (s.projectTimers[p.id] ?: TimerState()).elapsedMs(now)
                },
            )
        }
    }

    companion object {
        private const val TICK_MS = 500L

        /** 与 Navigation Compose 的 viewModel() 配合使用的工厂 */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DrumTrainerApp
                TrainingViewModel(
                    repository = app.container.trainingRepository,
                    savedStateHandle = createSavedStateHandle(),
                )
            }
        }
    }
}
