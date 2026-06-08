package me.langyue.autotranslation.config;

import me.langyue.autotranslation.AutoTranslation;
import me.langyue.autotranslation.translate.TranslatorManager;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import net.minecraft.world.InteractionResult;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@me.shedaniel.autoconfig.annotation.Config(name = AutoTranslation.MOD_ID)
public class Config implements ConfigData {

    public enum FilterMode {
        RESOURCE,
        CORRECTION
    }

    @Comment("筛选模式\n  RESOURCE: 只要当前语言存在 key，就忽略这个key，无论是否翻译\n  CORRECTION: 只要当前语言的 key 未翻译，就进行翻译，无论资源文件内是否存在")
    @ConfigEntry.Gui.RequiresRestart
    public FilterMode mode = FilterMode.RESOURCE;

    @Comment("英语特征 — 包含英文单词、不含格式化占位符(%s/%d)和翻译key(_)的文本")
    @ConfigEntry.Gui.RequiresRestart
    public String enFeature = "^(?!.*%[A-Za-z0-9])(?!.*_)(?=.*([A-Z]?[a-z]{2,})).+$";

    @Comment("您的语言的特征，默认的是中日韩")
    @ConfigEntry.Gui.RequiresRestart
    public String yourLanguageFeature = "[\\u0800-\\u9fa5\\uac00-\\ud7ff]+";

    @Comment("翻译引擎，默认谷歌翻译，可选 AAAABBBB")
    @ConfigEntry.Gui.RequiresRestart
    public String translator = TranslatorManager.DEFAULT_TRANSLATOR;

    @Comment("是否在翻译后的文本里增加原文显示")
    public boolean appendOriginal = true;

    @Comment("无需翻译文本, 支持正则, 不区分大小写")
    @ConfigEntry.Gui.RequiresRestart
    public List<String> wordBlacklist = new ArrayList<>();

    @Comment("开启 DEBUG 模式，开启可能会有日志刷屏")
    public boolean debug = false;

    @Comment("忽略的命名空间, 支持正则")
    @ConfigEntry.Gui.Excluded
    public Set<String> excludedNamespace = new HashSet<>() {{
        add("minecraft");
        add("^fabric-.*");
        add("forge");
        add("neoforge");
    }};

    @Comment("Google 翻译相关配置")
    @ConfigEntry.Gui.NoTooltip
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("google")
    @ConfigEntry.Gui.PrefixText
    public Google google = new Google();

    public static class Google {

        @Comment("Google 翻译备用域名，可以填镜像站，只要 API 跟谷歌相同就行")
        @ConfigEntry.Gui.RequiresRestart
        public String domain = "translate.google.com";

        @Comment("Google 服务器 IP，如果您所在地区无法直连域名，可以配置此项\n 参考 https://github.com/Ponderfly/GoogleTranslateIpCheck")
        @ConfigEntry.Gui.Excluded
        public Set<String> dns = new HashSet<>() {{
            add("64.233.189.191");
            add("108.177.97.100");
            add("216.239.32.40");
            add("74.125.196.113");
            add("142.251.171.90");
            add("142.250.1.90");
            add("172.217.218.90");
            add("108.177.126.90");
            add("142.251.1.90");
        }};
    }

    public static void init() {
        AutoConfig.register(Config.class, JanksonConfigSerializer::new);

        // 首次安装时根据游戏语言自动选择翻译域名
        Path configPath = FMLPaths.CONFIGDIR.get().resolve("autotranslation.json5");
        boolean isNewConfig = !Files.exists(configPath);

        AutoTranslation.CONFIG = AutoConfig.getConfigHolder(Config.class).getConfig();

        // Clean google domain
        cleanDomain();

        if (isNewConfig && "zh_cn".equalsIgnoreCase(AutoTranslation.getLanguage())) {
            AutoTranslation.CONFIG.google.domain = "google-translate-proxy.tantu.com";
            AutoConfig.getConfigHolder(Config.class).save();
        }

        // 配置保存时自动删除翻译缓存，确保新设置下次启动时生效
        AutoConfig.getConfigHolder(Config.class).registerSaveListener((holder, config) -> {
            deleteAutoTranslationFolder();
            return InteractionResult.SUCCESS;
        });
    }

    private static void cleanDomain() {
        if (AutoTranslation.CONFIG.google.domain != null) {
            AutoTranslation.CONFIG.google.domain = AutoTranslation.CONFIG.google.domain.toLowerCase();
            if (AutoTranslation.CONFIG.google.domain.startsWith("http")) {
                try {
                    AutoTranslation.CONFIG.google.domain = AutoTranslation.CONFIG.google.domain.split("//")[1];
                } catch (Throwable e) {
                    AutoTranslation.LOGGER.warn("Google domain format error");
                }
            }
        }
    }

    private static void deleteAutoTranslationFolder() {
        try {
            Path root = AutoTranslation.ROOT;
            if (Files.exists(root)) {
                Files.walk(root)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                AutoTranslation.LOGGER.info("AutoTranslation folder deleted due to config change");
            }
        } catch (Exception e) {
            AutoTranslation.LOGGER.warn("Failed to delete AutoTranslation folder", e);
        }
    }
}
