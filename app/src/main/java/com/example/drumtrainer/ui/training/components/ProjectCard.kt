package com.example.drumtrainer.ui.training.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.drumtrainer.R
import com.example.drumtrainer.data.local.entity.TrainingProject
import com.example.drumtrainer.util.formatDuration

/**
 * 单个项目卡片：标题 + 醒目字谱 + 独立正计时器（开始/暂停/重置）+ BPM 输入框（附上次 BPM 提示）。
 */
@Composable
fun ProjectCard(
    project: TrainingProject,
    elapsedMs: Long,
    isRunning: Boolean,
    bpmText: String,
    lastBpm: Int?,
    onToggleTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onBpmChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isRunning) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isRunning) 4.dp else 2.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // 标题 + 该项目耗时
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = formatDuration(elapsedMs),
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily.Monospace,
                    color = if (isRunning) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                )
            }

            // 醒目的练习内容 / 字谱
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = project.content,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Monospace,
                )
            }

            // 控制区：计时 + BPM
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onToggleTimer,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(if (isRunning) R.string.pause else R.string.start))
                }
                OutlinedButton(onClick = onResetTimer) {
                    Text(stringResource(R.string.reset))
                }
                if (project.needsBpm) {
                    val lastBpmValue = lastBpm
                    OutlinedTextField(
                        value = bpmText,
                        onValueChange = onBpmChange,
                        label = { Text(stringResource(R.string.bpm_label)) },
                        supportingText = if (lastBpmValue != null) {
                            { Text(stringResource(R.string.last_bpm, lastBpmValue)) }
                        } else {
                            null
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(104.dp),
                    )
                }
            }
        }
    }
}
