# 架子鼓训练记录 App —— 总体架构设计

## 1. 概述

一款安装在 Android 手机上的简易架子鼓训练记录与正计时 App。核心体验是「单页连续滑动」：
所有训练项目以长列表（`LazyColumn`）形式展现，像刷网页一样向上滑动浏览，顶部悬浮整套训练总计时器，
每个项目卡片内嵌独立正计时器与 BPM 输入框，底部一键提交今日训练。

## 2. 技术栈

| 项 | 选型 |
|---|---|
| 语言 | Kotlin |
| UI | Jetpack Compose + Material 3 |
| 数据库 | Room（本地持久化） |
| 异步 | Kotlin Coroutines + Flow |
| 状态 | ViewModel + StateFlow（单向数据流） |
| 架构 | 单 Activity + MVVM + 手动依赖注入（后续可换 Hilt） |

## 3. 分层架构

```
UI 层 (Compose)  →  ViewModel (StateFlow)  →  Repository  →  DAO  →  Room DB
```

- **UI 层**：只负责渲染与派发用户事件，通过 `collectAsStateWithLifecycle()` 订阅 `StateFlow`。
- **ViewModel**：持有 UI 状态与业务逻辑（计时器、互斥、BPM、提交），跨配置变更存活。
- **Repository**：唯一数据源，屏蔽 DAO 细节，集中事务/校验逻辑。
- **DAO + Room**：持久化训练模版与历史记录。

## 4. 完整文件结构（规划）

```
drum/
├── docs/
│   ├── architecture.md            # 本文件
│   └── SETUP.md                   # 工程搭建与依赖说明
├── app/src/main/
│   ├── AndroidManifest.xml        # 注册 DrumTrainerApp、MainActivity
│   ├── java/com/example/drumtrainer/
│   │   ├── DrumTrainerApp.kt      # Application + AppContainer（手动 DI）✅ 已实现
│   │   ├── MainActivity.kt        # Compose 入口 + Navigation   ⏳ 下一步
│   │   ├── core/
│   │   │   └── timer/
│   │   │       └── TimerState.kt  # 计时器状态模型              ✅ 已实现
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── entity/        # 4 个 Entity                 ✅ 已实现
│   │   │   │   │   ├── TrainingTemplate.kt
│   │   │   │   │   ├── TrainingProject.kt
│   │   │   │   │   ├── TrainingSession.kt
│   │   │   │   │   └── ProjectRecord.kt
│   │   │   │   ├── dao/           # 3 个 DAO                    ✅ 已实现
│   │   │   │   │   ├── TemplateDao.kt
│   │   │   │   │   ├── ProjectDao.kt
│   │   │   │   │   └── SessionDao.kt
│   │   │   │   ├── relation/
│   │   │   │   │   └── SessionWithRecords.kt                   ✅ 已实现
│   │   │   │   └── AppDatabase.kt                              ✅ 已实现
│   │   │   └── repository/
│   │   │       └── TrainingRepository.kt                       ✅ 已实现
│   │   ├── ui/
│   │   │   ├── theme/             # Color/Theme/Type            ⏳ 下一步
│   │   │   ├── navigation/        # AppNavHost（routes）        ⏳ 下一步
│   │   │   ├── training/
│   │   │   │   ├── TrainingScreen.kt        # LazyColumn 长列表 ⏳ 下一步
│   │   │   │   ├── TrainingViewModel.kt                       ✅ 已实现
│   │   │   │   └── components/
│   │   │   │       ├── TotalTimerBar.kt     # 顶部悬浮总计时    ⏳ 下一步
│   │   │   │       └── ProjectCard.kt       # 项目卡片          ⏳ 下一步
│   │   │   ├── template/
│   │   │   │   ├── TemplateListScreen.kt                        ⏳ 下一步
│   │   │   │   ├── TemplateEditScreen.kt                        ⏳ 下一步
│   │   │   │   └── TemplateViewModel.kt                         ⏳ 下一步
│   │   │   └── history/
│   │   │       ├── HistoryScreen.kt                             ⏳ 下一步
│   │   │       └── HistoryViewModel.kt                          ⏳ 下一步
│   │   └── util/
│   │       └── TimeFormatter.kt     # 毫秒 → mm:ss / h:mm:ss   ✅ 已实现
│   └── res/
│       ├── values/strings.xml       # 默认中文
│       ├── values-en/strings.xml    # 英文（可切换）
│       └── values/themes.xml
└── ...（Gradle 工程文件）
```

## 5. 数据模型与关系

```
TrainingTemplate 1 ──── N TrainingProject       (删除模版 → CASCADE 删除项目)
TrainingTemplate 1 ──── N TrainingSession       (删除模版 → SET_NULL，历史保留)
TrainingSession  1 ──── N ProjectRecord         (删除训练 → CASCADE 删除明细)
```

| 表 | 关键字段 | 说明 |
|---|---|---|
| `training_templates` | id, name, createdAt, updatedAt | 训练模版 |
| `training_projects` | id, templateId, title, content, needsBpm, sortOrder | 模版内的项目 |
| `training_sessions` | id, templateId?, templateName, startedAt, finishedAt, totalDurationMs | 一次训练 |
| `project_records` | id, sessionId, projectTitle, content, durationMs, bpm? | 项目明细快照 |

**快照策略**：历史记录中保存 `templateName`、`projectTitle`、`content` 的副本，
保证日后修改/删除模版时，历史记录内容不变。

## 6. 核心设计决策

1. **计时器基准用 `SystemClock.elapsedRealtime()`**：单调递增 + 后台/休眠仍计时，
   同时满足「后台继续按真实时间走」与「不受手动改时间影响」。`TimerState` 只存
   「基准 + 累计」，通过 `elapsedMs(now)` 折算当前耗时，天然支持任意时刻恢复计算。

2. **项目计时器互斥**：`runningProjectId` 记录当前在跑的项目；开始新项目时自动
   `pause` 上一个，符合「练完一项再练下一项」的习惯。

3. **总耗时 = 真实墙上时间**：总计时器独立于项目计时器，手动开始/暂停，提交时
   取 `totalTimer.elapsedMs()` 作为 `totalDurationMs`，包含休息时间。

4. **BPM 输入存字符串、提交时转 Int?**：`needsBpm=false` 的项目忽略输入；输入
   在 ViewModel 内过滤为纯数字，最多 4 位。

5. **提交原子性**：`SessionDao.insertSessionWithRecords` 用 `@Transaction` 一次性
   写入「session + N 条 project_records」。

6. **Flow 驱动 UI**：`Room → Flow → ViewModel StateFlow → Compose`，列表增删改与
   历史记录变化都会自动刷新界面。

## 7. 已完成（第一步）

数据库 Entity + DAO + Room、计时器模型、仓库层、训练页 ViewModel、依赖注入容器、
时长格式化工具、本架构文档与搭建说明。

## 8. 待实现（后续步骤）

1. Compose UI：`TrainingScreen`（顶部悬浮总计时 + `LazyColumn` 项目卡片 + 底部提交按钮）。
2. 模版管理页：列表 / 新建 / 编辑（增删项目、排序、设置 needsBpm）。
3. 历史页：按日期列表 + 展开查看项目耗时与 BPM。
4. 中英文双语资源与切换入口。
5. 数据库迁移配置（`exportSchema=true` + schemaLocation）。
