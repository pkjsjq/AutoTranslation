[English](#english) | [中文](#中文)

---

# English

# AutoTranslation Next

[![Modrinth](https://img.shields.io/badge/Modrinth-Download-00AF5C?logo=modrinth)](https://modrinth.com/mod/autotranslation)
[![CurseForge](https://img.shields.io/badge/CurseForge-Download-F16436?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/autotranslation)
[![GitHub](https://img.shields.io/badge/GitHub-Source-181717?logo=github)](https://github.com/LangYueMc/AutoTranslation)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A?logo=minecraft)](https://minecraft.net)
[![License](https://img.shields.io/badge/License-AGPL--3.0-blue)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-NeoForge-F16436)](https://neoforged.net)

Automatically translate untranslated language files in Minecraft, so you can enjoy any mod in your preferred language without waiting for manual translations or PRs to be merged.

- [Features](#features)
- [Dependencies](#dependencies)
- [Installation](#installation)
- [Commands](#commands)
- [Configuration](#configuration)
- [Translation API](#translation-api)
- [How It Works](#how-it-works)
- [License](#license)

## Features

### 🔤 Automatic Language File Translation

The core feature. When Minecraft loads resources, AutoTranslation Next detects language entries that are missing a translation for your current game language, translates them using the configured engine, and injects the translations at runtime — all without modifying any mod files.

- Works with **any mod** that uses standard Minecraft language files (`.json`)
- Translated files are saved in the `AutoTranslationNext/` folder under your game directory, where you can review, polish, and even repackage them into a resource pack
- Supports `/auto_translation reload` to hot-reload translations without restarting

### 🌐 Built-in Translation Engines

| Engine | Description |
|--------|-------------|
| **Google Translate** (default) | Uses `google-translate-proxy.tantu.com` — a China-accessible proxy. No DNS configuration or mirror setup needed. |
| **AAAABBBB** | Test-purpose translator. Always returns "AAAABBBB" for all inputs. Useful for verifying the translation pipeline is working correctly. |

### 🧠 Smart English Detection

A configurable regex pattern (default: `^(?!.*%[A-Za-z0-9])(?!.*_)(?=.*([A-Z]?[a-z]{2,})).+$`) ensures only actual English text gets translated, while skipping:

- Format specifiers (e.g., `%s`, `%1$d`)
- Translation keys (underscore-based identifiers)
- Already-localized entries

### ⚙️ Rich Configuration (Cloth Config)

All settings are configurable via the in-game mod menu (Cloth Config UI). Options include:

- Translation engine selection
- Source & target languages
- English-feature regex
- Filter mode (whitelist/blacklist)
- Word blacklist
- Debug mode

> **Auto cache cleanup:** Changing key settings (filter mode, regex, language, translator, word blacklist) automatically clears the translation cache and prompts you to restart for changes to take effect.

### 🪶 Zero Extra Dependencies

The HTTP client uses JDK's built-in `java.net.http.HttpClient` — no Apache HttpClient, no OkHttp, no extra libraries needed beyond the mod loader and Cloth Config.

### 📦 Resource Pack Packaging

Polish the auto-translated text and export it as a standard Minecraft resource pack:

```
/auto_translation pack_resource full        # Full export
/auto_translation pack_resource increment   # Incremental export (changes only)
```

This is especially useful for modpack creators and translation teams.

## Dependencies

| Dependency | Required | Notes |
|------------|----------|-------|
| [NeoForge](https://neoforged.net) | ✅ | Minecraft 1.21.1 |
| [Cloth Config API](https://modrinth.com/mod/cloth-config) | ✅ | v15+ |

## Installation

1. Install [NeoForge](https://neoforged.net) for Minecraft 1.21.1
2. Install [Cloth Config API](https://modrinth.com/mod/cloth-config)
3. Download the latest AutoTranslation Next `.jar` from [Modrinth](https://modrinth.com/mod/autotranslation) (recommended) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/autotranslation)
4. Place the `.jar` file in your `mods/` folder
5. Launch the game!

> 💡 For development builds, check [GitHub Actions](https://github.com/LangYueMc/AutoTranslation/actions) (requires GitHub login).

## Commands

All commands require confirmation first. Run `/auto_translation confirm` before executing any other command:

```
/auto_translation reload                    Reload translated resources
/auto_translation confirm                   Confirm pending command execution
/auto_translation pack_resource full        Export full resource pack
/auto_translation pack_resource increment   Export incremental resource pack
```

## Configuration

Open **Mods** → **AutoTranslation Next** → **Config** (requires Cloth Config API) to access all settings in-game. Alternatively, edit the config file at `config/autotranslation.json`.

Key configuration items:

- **Translation Engine** — Switch between Google Translate, AAAABBBB, or custom translators
- **Source Language** — The language to translate from (default: English)
- **Target Language** — Auto-detected from your game language setting
- **English Feature Regex** — Pattern to identify text that needs translation
- **Filter Mode** — Whitelist or blacklist specific mods/keys from translation
- **Word Blacklist** — Words or patterns to skip during translation

## Translation API

AutoTranslation Next has an extensible translator architecture. To add a custom translation engine:

1. Implement the `ITranslator` interface
2. Register it via `TranslatorManager.registerTranslator(String name, Supplier<ITranslator> getInstance)`
3. Select it in the config menu

```java
// Example: register a custom translator
TranslatorManager.registerTranslator("my_translator", MyTranslator::new);
```

Your translator will then appear as an option in the in-game config menu.

## How It Works

1. When Minecraft loads resources, AutoTranslation Next intercepts the language loading process
2. For each language entry that has no translation for the current game language, it checks the entry against the configurable English-feature regex
3. Matching entries are sent to the configured translation engine
4. Translated results are cached in the `AutoTranslationNext/` folder under your game directory
5. On subsequent launches, cached translations are loaded instantly — no re-translation needed

The `AutoTranslationNext/` folder contains:
- **Original files** (untranslated source text)
- **Translated files** (machine-translated output)

You can manually polish the translated files and use the `pack_resource` command to package them into a standard Minecraft resource pack for distribution.

## License

This project is licensed under the [GNU Affero General Public License v3.0](LICENSE).

---

# 中文

# AutoTranslation Next（自动翻译 Next）

[![Modrinth](https://img.shields.io/badge/Modrinth-下载-00AF5C?logo=modrinth)](https://modrinth.com/mod/autotranslation)
[![CurseForge](https://img.shields.io/badge/CurseForge-下载-F16436?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/autotranslation)
[![GitHub](https://img.shields.io/badge/GitHub-源码-181717?logo=github)](https://github.com/LangYueMc/AutoTranslation)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A?logo=minecraft)](https://minecraft.net)
[![License](https://img.shields.io/badge/License-AGPL--3.0-blue)](LICENSE)
[![Platform](https://img.shields.io/badge/平台-NeoForge-F16436)](https://neoforged.net)

自动翻译 Minecraft 中未翻译的语言文件，让你无需等待人工翻译或 PR 合并，就能用自己喜欢的语言畅玩任何模组。

- [前言](#前言)
- [功能](#功能)
- [前置模组](#前置模组)
- [安装](#安装)
- [游戏内指令](#游戏内指令)
- [配置](#配置)
- [翻译 API（开发者）](#翻译-api开发者)
- [工作原理](#工作原理)
- [开源协议](#开源协议)

## 前言

很多模组只有英文，即使向作者提交翻译 PR，更新周期也不确定，更有不少模组根本不接受翻译 PR。虽然已有「自动汉化更新」模组，但仍存在更新不及时、小众模组无人翻译、硬编码文本无法汉化等问题。

AutoTranslation Next 正是为解决这些痛点而生——它在游戏加载资源时自动检测并翻译缺失的语言条目，**不修改任何模组文件**，翻译结果缓存在本地，可随时润色、导出为资源包。

> ⚠️ **V2.0.0 重大变更：** 从 Architectury (Forge/Fabric) 迁移至纯 NeoForge，仅支持 Minecraft 1.21.1。**屏幕翻译功能已移除**，仅保留语言文件自动翻译。详见 [CHANGELOG](CHANGELOG.md)。

## 功能

### 🔤 自动翻译语言文件

核心功能。游戏加载资源时，自动检测缺少当前语言翻译的文本条目，调用翻译引擎进行翻译并注入——全程不修改任何模组文件。

- 兼容所有使用标准 Minecraft 语言文件（`.json`）的模组
- 翻译结果保存在游戏目录的 `AutoTranslationNext/` 文件夹中，可手动润色后打包为资源包——对整合包和汉化组尤其有用
- 支持 `/auto_translation reload` 热重载翻译，无需重启游戏

### 🌐 内置翻译引擎

| 引擎 | 说明 |
|------|------|
| **Google 翻译**（默认） | 使用 `google-translate-proxy.tantu.com` 代理，国内可直接访问，无需配置 DNS 或镜像站 |
| **AAAABBBB** | 测试用翻译器，所有翻译固定返回 "AAAABBBB"，方便验证翻译管线是否正常工作 |

### 🧠 智能英文识别

通过可配置的正则表达式精确识别需要翻译的英文文本。默认正则 `^(?!.*%[A-Za-z0-9])(?!.*_)(?=.*([A-Z]?[a-z]{2,})).+$` 会智能跳过：

- 格式化占位符（如 `%s`、`%1$d`）
- 翻译 key（下划线标识符）
- 已有翻译的条目

### ⚙️ 丰富的配置项

通过游戏内模组菜单（Cloth Config）即可配置所有参数：

- 翻译引擎切换
- 源语言 / 目标语言
- 英语特征正则表达式
- 筛选模式（白名单/黑名单）
- 单词黑名单
- 调试模式

> **自动清理缓存：** 修改筛选模式、正则、语言、翻译引擎、单词黑名单等关键设置时，自动删除翻译缓存并提示重启生效。

### 🪶 零额外依赖

HTTP 客户端采用 JDK 内置的 `java.net.http.HttpClient`，无需 Apache HttpClient、OkHttp 等任何额外网络库。

### 📦 资源包打包

润色翻译结果后，可直接导出为标准 Minecraft 资源包：

```
/auto_translation pack_resource full        # 全量打包
/auto_translation pack_resource increment   # 增量打包（仅变更部分）
```

对整合包作者和汉化组极其便利。

## 前置模组

| 前置 | 必须 | 备注 |
|------|------|------|
| [NeoForge](https://neoforged.net) | ✅ | Minecraft 1.21.1 |
| [Cloth Config API](https://modrinth.com/mod/cloth-config) | ✅ | v15+ |

## 安装

1. 安装 [NeoForge](https://neoforged.net)（Minecraft 1.21.1 版本）
2. 安装 [Cloth Config API](https://modrinth.com/mod/cloth-config)
3. 从 [Modrinth](https://modrinth.com/mod/autotranslation)（推荐）或 [CurseForge](https://www.curseforge.com/minecraft/mc-mods/autotranslation) 下载最新版 AutoTranslation Next
4. 将 `.jar` 文件放入 `mods/` 文件夹
5. 启动游戏！

> 💡 如需获取开发版，前往 [GitHub Actions](https://github.com/LangYueMc/AutoTranslation/actions)（需登录 GitHub 账号）。

## 游戏内指令

所有指令需要先确认执行。先运行 `/auto_translation confirm`，再执行其他指令：

```
/auto_translation reload                    重载翻译资源
/auto_translation confirm                   确认执行指令
/auto_translation pack_resource full        全量打包资源包
/auto_translation pack_resource increment   增量打包资源包
```

## 配置

在游戏中打开 **Mods** → **AutoTranslation Next** → **Config**（需安装 Cloth Config API）即可可视化配置。也可直接编辑 `config/autotranslation.json` 配置文件。

主要配置项：

- **翻译引擎** — 切换 Google、AAAABBBB 或自定义翻译器
- **源语言** — 待翻译的语言（默认：英语）
- **目标语言** — 自动跟随游戏语言设置
- **英语特征** — 用于识别需翻译文本的正则表达式
- **筛选模式** — 白名单/黑名单模式，控制哪些模组或 key 纳入/排除翻译
- **单词黑名单** — 跳过包含特定词汇的文本

## 翻译 API（开发者）

AutoTranslation Next 拥有可扩展的翻译器架构。添加自定义翻译引擎：

1. 实现 `ITranslator` 接口
2. 调用 `TranslatorManager.registerTranslator(String name, Supplier<ITranslator> getInstance)` 注册
3. 在配置菜单中即可选择你的翻译器

```java
// 示例：注册自定义翻译器
TranslatorManager.registerTranslator("my_translator", MyTranslator::new);
```

注册后，你的翻译器会自动出现在游戏内的翻译引擎选项中。

## 工作原理

1. Minecraft 加载资源时，AutoTranslation Next 拦截语言加载流程
2. 对每个缺少当前语言翻译的条目，使用可配置的正则表达式判断是否需要翻译
3. 匹配的条目发送至指定的翻译引擎进行翻译
4. 翻译结果缓存在游戏目录的 `AutoTranslationNext/` 文件夹中
5. 下次启动时直接从缓存加载，无需重复翻译

`AutoTranslationNext/` 文件夹内容：
- **原始文件** — 未翻译的源文本
- **翻译文件** — 机翻输出

你可以手动润色翻译文件，然后用 `pack_resource` 指令打包为标准 Minecraft 资源包进行分发。

## 开源协议

本项目基于 [GNU Affero General Public License v3.0](LICENSE) 开源。

---

[English](#english) | [中文](#中文)
