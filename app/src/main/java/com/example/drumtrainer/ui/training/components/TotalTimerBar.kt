package com.example.drumtrainer.ui.training.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import com.example.drumtrainer.R
import com.example.drumtrainer.util.formatDuration

/**
 * 顶部悬浮栏：展示训练组名称 + 整套训练总耗时，并提供开始/暂停/重置。
 * 放在 Scaffold 的 topBar 中，列表滚动时始终固定在顶部。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalTimerBar(
    templateName: String,
    totalElapsedMs: Long,
    isTotalRunning: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = templateName,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = formatDuration(totalElapsedMs),
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = FontFamily.Monospace,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                )
            }
        },
        actions = {
            TextButton(onClick = if (isTotalRunning) onPause else onStart) {
                Text(stringResource(if (isTotalRunning) R.string.pause else R.string.start))
            }
            TextButton(onClick = onReset) {
                Text(stringResource(R.string.reset))
            }
        },
    )
}
