package me.langyue.autotranslation.translate;

import me.langyue.autotranslation.AutoTranslation;
import me.langyue.autotranslation.translate.aaaabbbb.AaaaBbbb;
import me.langyue.autotranslation.translate.google.Google;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TranslatorManager {

    public static final String DEFAULT_TRANSLATOR = "Google";

    private static final Map<String, Supplier<ITranslator>> _TRANSLATOR_MAP = new LinkedHashMap<>() {{
        put("Google", Google::getInstance);
        put("AAAABBBB", AaaaBbbb::getInstance);
    }};

    private static final Map<String, ITranslator> _TRANSLATOR_INSTANCES = new HashMap<>();


    public static void init() {
        setTranslator(AutoTranslation.CONFIG.translator);
        TranslateThreadPool.init();
    }

    public static void setTranslator(String name) {
        if (!_TRANSLATOR_INSTANCES.containsKey(name)) {
            var supplier = _TRANSLATOR_MAP.get(name);
            if (supplier == null) {
                AutoTranslation.LOGGER.error("Unknown translator: {}", name);
                setTranslator(DEFAULT_TRANSLATOR);
            } else {
                ITranslator translator = supplier.get();
                if (translator == null) {
                    AutoTranslation.LOGGER.error("Failed to create translator: {}", name);
                    setTranslator(DEFAULT_TRANSLATOR);
                } else {
                    AutoTranslation.LOGGER.info("[DEBUG] Initializing translator: {} ({})", name, translator.getClass().getSimpleName());
                    translator.init();
                    AutoTranslation.LOGGER.info("[DEBUG] Translator {} ready: {}", name, translator.ready());
                    _TRANSLATOR_INSTANCES.put(name, translator);
                }
            }
        }
    }

    public static void registerTranslator(String name, Supplier<ITranslator> getInstance) {
        _TRANSLATOR_MAP.put(name, getInstance);
    }

    public static ITranslator getTranslator() {
        return getTranslator(AutoTranslation.CONFIG.translator);
    }

    public static ITranslator getTranslator(String name) {
        if (StringUtils.isBlank(name)) return null;
        return _TRANSLATOR_INSTANCES.get(name);
    }
}
