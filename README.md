# 架子鼓训练 Drum Trainer

一款安装在 Android 手机上的简易架子鼓训练记录与正计时 App。
技术栈：Kotlin + Jetpack Compose + Room + Navigation Compose。

## 功能

- 多套训练模版（训练组），可自定义项目（标题 / 练习字谱 / 是否记录 BPM）
- 单页连续滑动训练：顶部悬浮整套总计时 + `LazyColumn` 项目卡片（每个项目独立正计时 + BPM 输入）+ 底部一键提交
- 本地历史记录：按日期查看每次训练的各项目耗时与 BPM
- 中英双语（可切换）

## 用 GitHub Actions 自动打包 APK（推荐）

无需在本地安装 Android Studio：

- 推送代码到 `main` 分支 → 自动构建并上传 APK 产物（Artifact）
- 推送 `v*` 标签（如 `v1.0.0`）→ 自动构建并创建 **GitHub Release**，附带 APK 文件

APK 可在 GitHub 仓库的 **Actions** 页（构建产物 Artifact）或 **Releases** 页下载。

## 本地构建（可选）

需要 JDK 17 与 Android SDK。生成 Gradle Wrapper 后执行：

```bash
./gradlew assembleRelease
```

产物位于 `app/build/outputs/apk/release/`。

> 说明：当前 release 构建使用 debug 签名，仅供个人使用；上架发布前请替换为正式签名。
