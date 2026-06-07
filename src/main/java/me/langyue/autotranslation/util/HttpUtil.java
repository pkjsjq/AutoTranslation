package me.langyue.autotranslation.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.langyue.autotranslation.AutoTranslation;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.StringJoiner;

/**
 * HTTP 工具类，使用 java.net.http.HttpClient（零依赖）
 */
public class HttpUtil {

    private static final Gson GSON = new Gson();
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    private static volatile HttpClient httpClient;

    private static HttpClient getClient() {
        if (httpClient == null) {
            synchronized (HttpUtil.class) {
                if (httpClient == null) {
                    httpClient = HttpClient.newBuilder()
                            .connectTimeout(CONNECT_TIMEOUT)
                            .followRedirects(HttpClient.Redirect.NORMAL)
                            .build();
                }
            }
        }
        return httpClient;
    }

    /**
     * 重置 HTTP 客户端（DNS 变更后调用）
     */
    public static void reset() {
        synchronized (HttpUtil.class) {
            httpClient = null;
        }
    }

    /**
     * 发送 GET 请求
     *
     * @param url    请求 URL
     * @param dns    DNS 覆盖（可选，用 IP 替换 hostname 后设置 Host 头）
     * @param params 查询参数
     * @return 响应体字符串
     */
    public static String get(String url, String dns, Map<String, String> params) {
        try {
            String fullUrl = url;
            if (params != null && !params.isEmpty()) {
                StringJoiner sj = new StringJoiner("&");
                params.forEach((k, v) -> sj.add(encode(k) + "=" + encode(v)));
                fullUrl = url + "?" + sj;
            }

            URI uri = URI.create(fullUrl);
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(REQUEST_TIMEOUT)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .GET();

            // DNS 覆盖：将 IP 地址设置到 Host 头
            if (dns != null && !dns.isEmpty()) {
                builder.header("Host", uri.getHost());
                String newUrl = fullUrl.replace(uri.getHost(), dns);
                builder.uri(URI.create(newUrl));
            }

            HttpRequest request = builder.build();
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());

            AutoTranslation.debug("GET {} -> {} ({}ms)", fullUrl, response.statusCode(),
                    response.headers().firstValue("x-response-time").orElse("?"));
            return response.body();
        } catch (IOException | InterruptedException e) {
            AutoTranslation.LOGGER.error("HTTP GET failed: {}", url, e);
            return null;
        }
    }

    /**
     * 发送 POST 请求（form-urlencoded）
     *
     * @param url    请求 URL
     * @param params 表单参数
     * @param headers 额外请求头
     * @return 响应体字符串
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers) {
        try {
            StringJoiner sj = new StringJoiner("&");
            params.forEach((k, v) -> sj.add(encode(k) + "=" + encode(v)));
            byte[] body = sj.toString().getBytes(StandardCharsets.UTF_8);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(REQUEST_TIMEOUT)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(body));

            // 添加自定义请求头
            if (headers != null) {
                headers.forEach(builder::header);
            }

            // 默认 User-Agent
            if (headers == null || !containsIgnoreCase(headers, "User-Agent")) {
                builder.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            }

            HttpRequest request = builder.build();
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException e) {
            AutoTranslation.LOGGER.error("HTTP POST failed: {}", url, e);
            return null;
        }
    }

    /**
     * GET 请求并返回 JsonObject
     */
    public static JsonObject getJson(String url, String dns, Map<String, String> params) {
        String body = get(url, dns, params);
        if (body == null || body.isEmpty()) return null;
        try {
            return GSON.fromJson(body, JsonObject.class);
        } catch (Exception e) {
            AutoTranslation.LOGGER.error("JSON parse failed: {}", body, e);
            return null;
        }
    }

    /**
     * 简单 GET 状态检查
     */
    public static int status(String url, String dns) {
        try {
            URI uri = URI.create(url);
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(CONNECT_TIMEOUT)
                    .method("HEAD", HttpRequest.BodyPublishers.noBody());

            if (dns != null && !dns.isEmpty()) {
                builder.header("Host", uri.getHost());
                builder.uri(URI.create(url.replace(uri.getHost(), dns)));
            }

            HttpResponse<Void> response = getClient().send(builder.build(),
                    HttpResponse.BodyHandlers.discarding());
            return response.statusCode();
        } catch (Exception e) {
            AutoTranslation.LOGGER.warn("Status check failed: {} - {}", url, e.getMessage());
            return 999;
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static boolean containsIgnoreCase(Map<String, String> map, String key) {
        return map.keySet().stream().anyMatch(k -> k.equalsIgnoreCase(key));
    }
}
