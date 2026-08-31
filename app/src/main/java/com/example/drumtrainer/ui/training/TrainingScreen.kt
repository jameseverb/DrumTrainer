package com.example.drumtrainer.ui.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.drumtrainer.R
import com.example.drumtrainer.ui.training.components.ProjectCard
import com.example.drumtrainer.ui.training.components.TotalTimerBar

/**
 * 单页连续滑动训练界面（核心体验）。
 *
 * - 顶部悬浮：整套训练总计时器（不随列表滚动）
 * - 主体：LazyColumn 长列表，所有项目卡片垂直排列、顺滑滑动
 * - 列表最底部：「完成今日训练」按钮，一次性提交所有 BPM 与耗时
 */
@Composable
fun TrainingScreen(
    viewModel: TrainingViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // 提交结果提示
    LaunchedEffect(uiState.submitState) {
        when (uiState.submitState) {
            SubmitState.Success -> {
                snackbarHostState.showSnackbar(stringResource(R.string.saved_success))
                viewModel.consumeSubmitResult()
            }

            SubmitState.Error -> {
                snackbarHostState.showSnackbar(stringResource(R.string.save_failed))
                viewModel.consumeSubmitResult()
            }

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TotalTimerBar(
                templateName = uiState.template?.name.orEmpty(),
                totalElapsedMs = uiState.totalElapsedMs,
                isTotalRunning = uiState.totalTimer.isRunning,
                onStart = viewModel::startTotal,
                onPause = viewModel::pauseTotal,
                onReset = viewModel::resetTotal,
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // 项目卡片长列表
            items(uiState.projects, key = { it.id }) { project ->
                ProjectCard(
                    project = project,
                    elapsedMs = uiState.projectElapsedMs[project.id] ?: 0L,
                    isRunning = uiState.runningProjectId == project.id,
                    bpmText = uiState.bpmInputs[project.id].orEmpty(),
                    onToggleTimer = { viewModel.toggleProjectTimer(project.id) },
                    onResetTimer = { viewModel.resetProjectTimer(project.id) },
                    onBpmChange = { viewModel.setBpm(project.id, it) },
                )
            }

            // 列表最底部：完成按钮
            item(key = "complete_button") {
                CompleteButton(
                    isSaving = uiState.submitState == SubmitState.Saving,
                    enabled = uiState.projects.isNotEmpty(),
                    onClick = viewModel::submitSession,
                )
            }
        }
    }
}

@Composable
private fun CompleteButton(
    isSaving: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isSaving,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Text(
            text = stringResource(if (isSaving) R.string.saving else R.string.complete_training),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
