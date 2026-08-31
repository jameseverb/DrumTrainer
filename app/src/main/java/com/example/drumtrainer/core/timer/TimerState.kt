package com.example.drumtrainer.core.timer

import android.os.SystemClock

/**
 * 单个正计时器的不可变状态快照。
 *
 * 时间基准选用 [SystemClock.elapsedRealtime]：
 * - 单调递增，不受用户手动修改系统时间影响；
 * - 即使 App 进入后台或设备休眠，时间仍在真实流逝（满足「后台继续按真实时间走」的需求）。
 *
 * 用法：状态本身只保存「基准 + 累计」，通过 [elapsedMs] 在任意时刻折算当前耗时。
 */
data class TimerState(
    val isRunning: Boolean = false,
    /** 本次运行开始时的 elapsedRealtime 基准（仅 isRunning=true 时有意义） */
    val startedAtRealtimeMs: Long = 0L,
    /** 暂停前已累计的毫秒数 */
    val accumulatedMs: Long = 0L,
) {
    /** 折算到 nowRealtimeMs 时刻的当前耗时 */
    fun elapsedMs(nowRealtimeMs: Long = SystemClock.elapsedRealtime()): Long =
        accumulatedMs + if (isRunning) (nowRealtimeMs - startedAtRealtimeMs).coerceAtLeast(0L) else 0L

    /** 开始计时（幂等） */
    fun start(nowRealtimeMs: Long): TimerState =
        if (isRunning) this
        else TimerState(isRunning = true, startedAtRealtimeMs = nowRealtimeMs, accumulatedMs = accumulatedMs)

    /** 暂停计时（幂等，把当前耗时并入累计值） */
    fun pause(nowRealtimeMs: Long): TimerState =
        if (!isRunning) this
        else TimerState(isRunning = false, accumulatedMs = elapsedMs(nowRealtimeMs))

    /** 重置归零 */
    fun reset(): TimerState = TimerState()
}
