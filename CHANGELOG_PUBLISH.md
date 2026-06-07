# V2.0.0 — AutoTranslation Next 正式发布

## 变更

1. **平台迁移**: 从 Architectury (Forge/Fabric) 迁移到纯 NeoForge，支持 Minecraft 1.21.1；
2. **移除屏幕翻译**: 完全移除屏幕翻译功能，仅保留语言文件自动翻译；
3. **新增 AAAABBBB 翻译器**: 测试用翻译器，方便验证翻译管线；
4. **Google 翻译优化**: 默认域名改为 `google-translate-proxy.tantu.com`，国内可直接访问；
5. **重写 HTTP 客户端**: 使用 JDK 内置 HttpClient，零额外依赖；
6. **优化英语特征正则**: 排除格式化占位符和翻译 key 的误匹配；
7. **配置变更自动清理**: 修改关键设置时自动删除翻译缓存并提示重启；
8. **移除 Fabric/Forge 支持**: 不再支持 Fabric 和 Forge 平台，仅 NeoForge；
9.  **修复多个关键 Bug**: Mixin 静默不生效、命令事件总线错误、游戏崩溃等。

[完整更新日志](https://github.com/LangYueMc/AutoTranslation/blob/master/CHANGELOG.md)
- - -

## Changed

1. **Platform Migration**: Migrated from Architectury (Forge/Fabric) to pure NeoForge, supporting Minecraft 1.21.1;
2. **Screen Translation Removed**: Completely removed screen translation, keeping only language file auto-translation;
3. **New AAAABBBB Translator**: Test-purpose translator for pipeline verification;
4. **Google Optimized**: Default domain changed to `google-translate-proxy.tantu.com`, accessible from China;
5. **Rewritten HTTP Client**: Switched to JDK built-in HttpClient, zero extra dependencies;
6. **Optimized English Regex**: Exclude format specifiers and translation keys from false positives;
7. **Auto Cache Cleanup**: Modifying key settings automatically deletes cache with restart reminder;
8. **Dropped Fabric/Forge**: No longer supports Fabric or Forge, NeoForge only;
9.  **Multiple Critical Fixes**: Silent Mixin failure, command event bus error, game crash, etc.

[Full CHANGELOG](https://github.com/LangYueMc/AutoTranslation/blob/master/CHANGELOG_en.md)
