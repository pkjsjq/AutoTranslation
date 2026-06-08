package me.langyue.autotranslation.translate.google;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import me.langyue.autotranslation.AutoTranslation;
import me.langyue.autotranslation.translate.ITranslator;
import me.langyue.autotranslation.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class Google implements ITranslator {

    private static final Google INSTANCE = new Google();
    private static final String DEFAULT_DOMAIN = "translate.google.com";
    private static final Gson GSON = new Gson();

    private String domain = DEFAULT_DOMAIN;
    private String currentDns = null;
    private boolean ready = false;

    private Google() {
    }

    public static Google getInstance() {
        return INSTANCE;
    }

    @Override
    public void init() {
        this.domain = AutoTranslation.CONFIG.google.domain;
        if (this.domain == null || this.domain.isEmpty()) {
            this.domain = DEFAULT_DOMAIN;
        }
        chooseBestDns();
        this.ready = true;
    }

    private void chooseBestDns() {
        if (AutoTranslation.CONFIG.google.dns != null && !AutoTranslation.CONFIG.google.dns.isEmpty()) {
            for (String ip : AutoTranslation.CONFIG.google.dns) {
                int status = HttpUtil.status("https://" + domain + "/?t=" + System.currentTimeMillis(), ip);
                if (status < 400) {
                    this.currentDns = ip;
                    AutoTranslation.LOGGER.info("Google DNS selected: {}", ip);
                    return;
                }
            }
        }
        // 直接连接
        int status = HttpUtil.status("https://" + domain + "/?t=" + System.currentTimeMillis(), null);
        if (status < 400 || status == 999) {
            this.currentDns = null;
        }
    }

    @Override
    public boolean ready() {
        return ready;
    }

    @Override
    public int maxLength() {
        return 5000;
    }

    @Override
    public String translate(String text, String tl, String sl) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("client", "gtx");
            params.put("sl", sl);
            params.put("tl", tl);
            params.put("dt", "t");
            params.put("ie", "UTF-8");
            params.put("oe", "UTF-8");
            params.put("q", text);

            String body = HttpUtil.get("https://" + domain + "/translate_a/single", currentDns, params);
            if (body == null) return null;

            JsonArray jsonArray = GSON.fromJson(body, JsonArray.class);
            if (jsonArray == null) return null;

            JsonArray elements = jsonArray.getAsJsonArray().get(0).getAsJsonArray();
            StringBuilder result = new StringBuilder();
            elements.forEach(jsonElement -> {
                result.append(
                        jsonElement.getAsJsonArray().get(0).getAsString()
                                .replaceAll("​", "")
                                .replaceAll("\\\\[“”]", "`")
                );
            });
            return result.toString();
        } catch (Throwable e) {
            AutoTranslation.LOGGER.error("Google Translate exception", e);
            return null;
        }
    }
}
