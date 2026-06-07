package me.langyue.autotranslation.translate.aaaabbbb;

import me.langyue.autotranslation.translate.ITranslator;

/**
 * AAAABBBB 翻译器 — 所有翻译结果固定返回 "AAAABBBB"
 */
public class AaaaBbbb implements ITranslator {

    private static final AaaaBbbb INSTANCE = new AaaaBbbb();

    private AaaaBbbb() {
    }

    public static AaaaBbbb getInstance() {
        return INSTANCE;
    }

    @Override
    public void init() {
    }

    @Override
    public int maxLength() {
        return 5000;
    }

    @Override
    public String translate(String text, String tl, String sl) {
        if (text == null || text.isEmpty()) return "AAAABBBB";
        // 批量翻译：保持行数一致，每行对应 "AAAABBBB"
        if (text.contains("\n")) {
            String[] lines = text.split("\n", -1);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                sb.append("AAAABBBB");
                if (i < lines.length - 1) sb.append("\n");
            }
            return sb.toString();
        }
        return "AAAABBBB";
    }
}
