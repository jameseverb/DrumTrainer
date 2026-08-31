# 构建与发布说明

工程已补齐为**完整的 Gradle 项目**（含 `settings.gradle.kts`、`gradle/libs.versions.toml`、
`app/build.gradle.kts`、Manifest、资源与全部源码），**无需在本地安装 Android Studio**。

## 一、用 GitHub Actions 自动打包 APK（推荐）

仓库已内置 `.github/workflows/build.yml`，触发规则：

| 触发 | 行为 |
|---|---|
| 推送到 `main` 分支 | 自动构建 release APK，并上传为 Artifact |
| 推送 `v*` 标签（如 `v1.0.0`） | 自动构建并创建 **GitHub Release**，附带 APK |
| 手动（Actions → Build APK → Run workflow） | 同上（构建并上传 Artifact） |

### 使用步骤

1. 在 GitHub 新建仓库，把本项目推上去。
2. 打开仓库 **Actions** 页，确认 workflow 已启用。
3. 推送代码到 `main` → 等待 `Build APK` 完成。
4. 下载 APK：
   - 日常构建：Actions 页 → 某次运行 → **Artifacts** 区的 `drumtrainer-release-apk`；
   - 发布版本：打标签并推送 `git tag v1.0.0 && git push origin v1.0.0`，
     到 **Releases** 页下载附带的 APK。

### workflow 做了什么

- 安装 JDK 17（Temurin）
- 安装 Android SDK（`platforms;android-35` + `build-tools;35.0.0`）
- 通过 `gradle/actions/setup-gradle@v4` 下载 Gradle 8.9（仓库未内置 wrapper）
- 执行 `gradle assembleRelease`
- 上传 / 发布 APK

## 二、本地构建（可选）

需要 JDK 17 与 Android SDK。先生成 Gradle Wrapper，再构建：

```bash
gradle wrapper --gradle-version 8.9
./gradlew assembleRelease
```

产物位于 `app/build/outputs/apk/release/`。

## 三、签名说明

当前 `release` 构建复用 **debug 签名**（见 `app/build.gradle.kts`），仅供个人安装使用。
若要上架发布，请改为正式 keystore，并在 GitHub Secrets 中配置签名密钥。

## 四、依赖版本要点

- Kotlin `2.0.21` ↔ KSP `2.0.21-1.0.28`（**两者必须严格对应**）
- AGP `8.7.3` ↔ Gradle `8.9`
- Compose BOM `2024.12.01`、Room `2.6.1`、Navigation Compose `2.8.5`
- `templateId` 导航参数以 `NavType.LongType` 传递，由 `SavedStateHandle` 注入 ViewModel
