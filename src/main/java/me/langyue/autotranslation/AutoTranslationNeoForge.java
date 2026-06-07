package me.langyue.autotranslation;

import me.langyue.autotranslation.command.AutoTranslationCommands;
import me.langyue.autotranslation.config.Config;
import me.langyue.autotranslation.resource.ResourceManager;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.locale.Language;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

import java.util.Map;

@Mod(AutoTranslation.MOD_ID)
public class AutoTranslationNeoForge {

    private boolean translationInitDone = false;

    public AutoTranslationNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(this::registerKeyMappings);
        NeoForge.EVENT_BUS.register(this);

        AutoTranslation.init();

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (mc, screen) -> AutoConfig.getConfigScreen(Config.class, screen).get()
        );
    }

    private void registerKeyMappings(RegisterKeyMappingsEvent event) {
    }

    @SubscribeEvent
    private void registerCommands(RegisterCommandsEvent event) {
        AutoTranslationCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        AutoTranslation.stop();
    }

    /**
     * 在首个客户端 tick 扫描翻译表，替代无法注入的 ClientLanguageMixin
     */
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        if (translationInitDone) return;
        translationInitDone = true;

        try {
            Language lang = Language.getInstance();
            AutoTranslation.LOGGER.info("[DEBUG] ClientTick scan: current lang={}, default={}",
                    Minecraft.getInstance().options.languageCode, Language.DEFAULT);

            if (AutoTranslation.getLanguage().equals(Language.DEFAULT)) {
                AutoTranslation.LOGGER.info("[DEBUG] ClientTick scan: default language, skip");
                return;
            }

            // 获取完整的翻译表
            Map<String, String> storage = lang.getLanguageData();
            AutoTranslation.LOGGER.info("[DEBUG] ClientTick scan: storage size={}", storage.size());

            if (storage.isEmpty()) {
                AutoTranslation.LOGGER.warn("[DEBUG] ClientTick scan: empty storage, abort");
                return;
            }

            ResourceManager.setLanguage(lang);
            ResourceManager.UNKNOWN_KEYS.clear();

            int checked = 0;
            int found = 0;
            for (Map.Entry<String, String> entry : storage.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (TranslatorHelper.shouldTranslate(key, value)) {
                    ResourceManager.UNKNOWN_KEYS.put("game", key);
                    found++;
                }
                checked++;
            }

            AutoTranslation.LOGGER.info("[DEBUG] ClientTick scan done: checked={}, found={}", checked, found);

            if (!ResourceManager.UNKNOWN_KEYS.isEmpty()) {
                AutoTranslation.LOGGER.info("[DEBUG] ClientTick scan: starting initResource for {} keys",
                        ResourceManager.UNKNOWN_KEYS.size());
                ResourceManager.initResource();
            } else {
                AutoTranslation.LOGGER.info("[DEBUG] ClientTick scan: no untranslated keys found");
            }
        } catch (Throwable e) {
            AutoTranslation.LOGGER.error("[DEBUG] ClientTick scan failed", e);
        }
    }
}
