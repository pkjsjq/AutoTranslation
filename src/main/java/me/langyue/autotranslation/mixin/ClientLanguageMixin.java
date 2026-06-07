package me.langyue.autotranslation.mixin;

import me.langyue.autotranslation.TranslatorHelper;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 用 @Overwrite 替换 getOrDefault，不依赖任何 @Shadow/@Invoker/@Inject 注入点。
 * @Overwrite 直接替换整个方法实现，100% 可靠。
 */
@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {

    /**
     * 替换 ClientLanguage.getOrDefault()。
     * 先走原始逻辑，有缓存时通过 translate() 读取（含 appendOriginal 原文追加）。
     * hasCache 作为守门不排队新翻译，批量翻译由 initResource 负责。
     */
    @Overwrite
    public String getOrDefault(String key, String defaultValue) {
        String value = ((ClientLanguage) (Object) this).getLanguageData().getOrDefault(key, defaultValue);
        if (TranslatorHelper.hasCache(key)) {
            // translate(key, en, callback=null) 命中缓存时不会排队，只返回带原文追加的结果
            String translated = TranslatorHelper.translate(key, value, null);
            return translated != null ? translated : value;
        }
        return value;
    }
}
