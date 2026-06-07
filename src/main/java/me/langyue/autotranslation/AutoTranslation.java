package me.langyue.autotranslation;

import me.langyue.autotranslation.config.Config;
import me.langyue.autotranslation.resource.ResourceManager;
import me.langyue.autotranslation.translate.TranslatorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class AutoTranslation {
    public static final Logger LOGGER = LoggerFactory.getLogger("AutoTranslation");
    public static final String MOD_ID = "autotranslation";

    public static final Path ROOT = FMLPaths.GAMEDIR.get().resolve("AutoTranslation");
    public static Config CONFIG = null;

    public static void init() {
        Config.init();
        TranslatorManager.init();
        TranslatorHelper.init();
        ResourceManager.init();
    }

    public static void stop() {
        ResourceManager.save();
    }

    public static String getLanguage() {
        try {
            return Minecraft.getInstance().options.languageCode;
        } catch (Throwable e) {
            return Language.DEFAULT;
        }
    }

    public static void debug(String var1, Object... var2) {
        if (CONFIG != null && CONFIG.debug) {
            LOGGER.info(var1, var2);
        }
    }
}
