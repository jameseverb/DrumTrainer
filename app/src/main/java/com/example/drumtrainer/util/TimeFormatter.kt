package com.example.drumtrainer.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 把毫秒格式化为计时器显示文本。
 * 不足 1 小时显示 mm:ss；达到 1 小时显示 h:mm:ss。
 */
fun formatDuration(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0L)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

private val DATE_TIME_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

/** 把 epoch millis 格式化为「yyyy-MM-dd HH:mm」（本地时区，minSdk 26 起可用 java.time） */
fun formatDateTime(epochMs: Long): String =
    Instant.ofEpochMilli(epochMs).atZone(ZoneId.systemDefault()).format(DATE_TIME_FORMATTER)
