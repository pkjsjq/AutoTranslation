# V2.1.0

2026-06-08

## 变更

1. **智能翻译域名选择**: 首次安装时自动根据游戏语言选择最优 Google 翻译域名——简体中文（zh_cn）使用国内代理 `google-translate-proxy.tantu.com`，其他语言使用 `translate.google.com`；
2. **默认域名调整**: Java 默认域名和兜底常量改为 `translate.google.com`，避免非中国用户被误导到国内代理。

# V2.0.0 — AutoTranslation Next 正式发布

2026-06-07

## 变更

1. **平台迁移**: 从 Architectury (Forge/Fabric) 迁移到纯 NeoForge，支持 Minecraft 1.21.1；
2. **精简项目结构**: 移除多平台架构（common/fabric/forge），改为单项目 Gradle 构建；
3. **移除屏幕翻译**: 完全移除屏幕翻译功能（快捷键翻译、翻译图标、屏幕翻译配置），仅保留语言文件自动翻译；
4. **新增 AAAABBBB 翻译器**: 测试用翻译器，所有翻译固定返回 "AAAABBBB"，方便验证翻译管线；
5. **Google 翻译优化**: 默认域名改为 `google-translate-proxy.tantu.com`（国内可直接访问），Google 作为默认翻译引擎；
6. **重写 HTTP 客户端**: 从 Apache HttpClient 迁移至 JDK 内置 `java.net.http.HttpClient`，零额外依赖；
7. **优化英语特征正则**: 默认正则改为 `^(?!.*%[A-Za-z0-9])(?!.*_)(?=.*([A-Z]?[a-z]{2,})).+$`，排除格式化占位符和翻译 key 的误匹配；
8. **配置变更自动清理**: 修改筛选模式、英语特征、语言特征、翻译引擎、单词黑名单时，自动删除翻译缓存并提示重启生效；
9.  **移除 Patchouli/FTB 兼容**: 移除 Fabric 平台的兼容层代码，精简项目；
10. **移除 Fabric/Forge 支持**: 不再支持 Fabric 和 Forge 平台，仅支持 NeoForge。

# V1.2.1

2023-12-30

## 修复

1. 修复自动选择 DNS 后仍然无法翻译的问题。
