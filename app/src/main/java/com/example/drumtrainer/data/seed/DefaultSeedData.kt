package com.example.drumtrainer.data.seed

/**
 * 预置训练数据结构。
 */
data class DefaultProject(
    val title: String,
    val content: String,
    val needsBpm: Boolean,
    val sortOrder: Int,
)

/**
 * App 首次启动时预置的一套基础训练模板（热身 → 双跳入门 → 单音金字塔）。
 * 仅在检测到尚无同名模板时插入，不会覆盖用户已创建的模板。
 */
object DefaultSeedData {

    val projects = listOf(
        DefaultProject(
            title = "阶段一·单手独立发力",
            content = """打法：右手 8 下 + 左手 8 下（RRRRRRRR LLLLLLLL），8 分音符
速度：80 - 100 BPM
时间：约 5 分钟
要点：观察双手抬起高度与音量是否一致；左手用手腕带动，不要靠手臂硬砸""",
            needsBpm = true,
            sortOrder = 0,
        ),
        DefaultProject(
            title = "阶段一·左手专项加练",
            content = """打法：左手带头 16 分音符单音（LRLR LRLR）
速度：从 85 BPM 起逐步加至 100 BPM
时间：连续 3 - 5 分钟
要点：Left Hand Lead，左手主导""",
            needsBpm = true,
            sortOrder = 1,
        ),
        DefaultProject(
            title = "阶段二·一落一抓弹跳",
            content = """原理：第一音放松手腕让鼓棒自然弹起，第二音用中指 / 无名指 / 小指轻轻收握打出（Drop & Catch）
要点：学会利用弹跳 Rebound，是突破 120+ BPM 单音与双音提速的必经之路""",
            needsBpm = false,
            sortOrder = 2,
        ),
        DefaultProject(
            title = "阶段二·双音分解基础",
            content = """节奏：RRLL RRLL RRLL RRLL（16 分音符）
速度：从 50 - 60 BPM 开始，每天递增 2 - 3 BPM，两周目标 80 BPM
要点：不要快，先保证第二音与第一音音量一样大""",
            needsBpm = true,
            sortOrder = 3,
        ),
        DefaultProject(
            title = "阶段三·单音速度金字塔",
            content = """打法：16 分音符单音（RLRL RLRL），按金字塔阶梯练习
第 1 阶 90 BPM × 1 分钟（找松弛感）
第 2 阶 100 BPM × 45 秒（保持清晰）
第 3 阶 110 BPM × 30 秒（接近极限）
第 4 阶 118 - 122 BPM × 15 秒（冲刺突破极限）
第 5 阶 90 BPM × 1 分钟（降速放松肌肉）
循环 2 - 3 组，组间休息 1 分钟""",
            needsBpm = true,
            sortOrder = 4,
        ),
    )
}
