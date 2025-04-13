package com.chandra.animasu;

import android.content.Context;
import android.util.Log;
import android.webkit.WebResourceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class AdBlocker {
    private static final String TAG = "AdBlocker";
    private static final Set<String> AD_HOSTS = new HashSet<>();
    private static final Set<String> AD_CLASSES = new HashSet<>();
    private static final Set<String> AD_IDS = new HashSet<>();
    private static final List<Pattern> URL_PATTERNS = new ArrayList<>();
    private static final Set<String> CONTENT_KEYWORDS = new HashSet<>();
    
    private static boolean isInitialized = false;

    /**
     * Inisialisasi AdBlocker dengan aturan dari file JSON
     * @param context Context aplikasi
     */
    public static synchronized void initialize(Context context) {
        if (isInitialized) {
            return;
        }
        
        try {
            // Baca file aturan dari assets
            InputStream is = context.getAssets().open("adblocker_rules.json");
            Scanner scanner = new Scanner(is, "UTF-8");
            StringBuilder jsonString = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonString.append(scanner.nextLine());
            }
            scanner.close();
            
            // Parse JSON rules
            JSONObject rules = new JSONObject(jsonString.toString());
            
            // Muat class selectors
            JSONArray classSelectors = rules.getJSONArray("class_selectors");
            for (int i = 0; i < classSelectors.length(); i++) {
                AD_CLASSES.add(classSelectors.getString(i));
            }
            
            // Muat ID selectors
            JSONArray idSelectors = rules.getJSONArray("id_selectors");
            for (int i = 0; i < idSelectors.length(); i++) {
                AD_IDS.add(idSelectors.getString(i));
            }
            
            // Muat domain blocklist
            JSONArray domainBlocklist = rules.getJSONArray("domain_blocklist");
            for (int i = 0; i < domainBlocklist.length(); i++) {
                AD_HOSTS.add(domainBlocklist.getString(i));
            }
            
            // Muat URL pattern blocklist
            JSONArray urlPatterns = rules.getJSONArray("url_pattern_blocklist");
            for (int i = 0; i < urlPatterns.length(); i++) {
                String patternStr = urlPatterns.getString(i)
                        .replace(".", "\\.")
                        .replace("*", ".*");
                URL_PATTERNS.add(Pattern.compile(patternStr));
            }
            
            // Muat keyword konten
            if (rules.has("content_keywords")) {
                JSONArray contentKeywords = rules.getJSONArray("content_keywords");
                for (int i = 0; i < contentKeywords.length(); i++) {
                    CONTENT_KEYWORDS.add(contentKeywords.getString(i).toLowerCase());
                }
            }
            
            isInitialized = true;
            Log.d(TAG, "AdBlocker initialized with " + AD_HOSTS.size() + " domains, " + 
                    AD_CLASSES.size() + " classes, " + AD_IDS.size() + " IDs, " +
                    URL_PATTERNS.size() + " URL patterns, and " +
                    CONTENT_KEYWORDS.size() + " content keywords");
            
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error initializing AdBlocker", e);
            
            // Fallback initialization with basic rules
            fallbackInitialization();
        }
    }
    
    /**
     * Inisialisasi dasar jika file aturan tidak dapat dibaca
     */
    private static void fallbackInitialization() {
        // Kelas iklan dari website
        AD_CLASSES.add("lmd-iklan");
        AD_CLASSES.add("ads-area");
        AD_CLASSES.add("banner-ads");

        // Domain iklan umum
        AD_HOSTS.add("doubleclick.net");
        AD_HOSTS.add("googlesyndication.com");
        AD_HOSTS.add("googleadservices.com");
        AD_HOSTS.add("moatads.com");
        AD_HOSTS.add("adnxs.com");
        AD_HOSTS.add("advertising.com");
        
        // Kata kunci konten
        CONTENT_KEYWORDS.add("judi online");
        CONTENT_KEYWORDS.add("slot gacor");
        CONTENT_KEYWORDS.add("togel online");
        
        isInitialized = true;
        Log.d(TAG, "AdBlocker initialized with fallback rules");
    }

    /**
     * Periksa apakah URL tersebut adalah iklan
     * @param url URL yang akan diperiksa
     * @return true jika URL adalah iklan, false jika bukan
     */
    public static boolean isAd(String url) {
        try {
            if (url == null) {
                return false;
            }

            // Periksa pola URL yang diblokir
            for (Pattern pattern : URL_PATTERNS) {
                if (pattern.matcher(url).matches()) {
                    Log.d(TAG, "Blocked ad URL by pattern: " + url);
                    return true;
                }
            }

            // Periksa URL untuk kata kunci konten
            String lowerUrl = url.toLowerCase();
            for (String keyword : CONTENT_KEYWORDS) {
                if (lowerUrl.contains(keyword.replace(" ", "")) || 
                    lowerUrl.contains(keyword.replace(" ", "-")) ||
                    lowerUrl.contains(keyword.replace(" ", "_"))) {
                    Log.d(TAG, "Blocked URL by content keyword: " + url + " (matched: " + keyword + ")");
                    return true;
                }
            }

            URL parsedUrl = new URL(url);
            String host = parsedUrl.getHost();

            if (host == null) {
                return false;
            }

            // Periksa domain yang diblokir
            for (String adHost : AD_HOSTS) {
                if (host.contains(adHost)) {
                    Log.d(TAG, "Blocked ad URL by domain: " + url);
                    return true;
                }
            }
            return false;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error parsing URL: " + url, e);
            return false;
        }
    }

    /**
     * Buat respons kosong untuk memblokir permintaan sumber daya
     */
    public static WebResourceResponse createEmptyResource() {
        return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream("".getBytes()));
    }

    /**
     * Buat respons error untuk menampilkan halaman error ketika konten diblokir
     */
    public static WebResourceResponse createErrorPageResponse(Context context) {
        try {
            InputStream errorHtml = context.getAssets().open("html/error_page.html");
            return new WebResourceResponse("text/html", "UTF-8", errorHtml);
        } catch (IOException e) {
            Log.e(TAG, "Error loading error page", e);
            return createEmptyResource();
        }
    }

    /**
     * Proses konten HTML untuk menghapus elemen iklan
     * @param html Konten HTML yang akan diproses
     * @return HTML yang telah dibersihkan dari iklan
     */
    public static String processHtmlContent(String html) {
        try {
            Document doc = Jsoup.parse(html);
            
            // Hapus elemen berdasarkan kelas
            for (String className : AD_CLASSES) {
                Elements adElements = doc.getElementsByClass(className);
                for (Element element : adElements) {
                    element.remove();
                    Log.d(TAG, "Removed element with class: " + className);
                }
            }
            
            // Hapus elemen berdasarkan ID
            for (String idName : AD_IDS) {
                Elements idElements = doc.getElementsByAttributeValueContaining("id", idName);
                for (Element element : idElements) {
                    element.remove();
                    Log.d(TAG, "Removed element with ID containing: " + idName);
                }
            }
            
            // Hapus iframe yang mungkin mengandung iklan
            Elements iframes = doc.select("iframe[src*=doubleclick],iframe[src*=googlead],iframe[src*=adnxs]");
            for (Element iframe : iframes) {
                iframe.remove();
                Log.d(TAG, "Removed iframe: " + iframe.attr("src"));
            }
            
            // Hapus div dengan nama kelas yang mungkin terkait iklan
            Elements adDivs = doc.select("div[class*=iklan],div[class*=ads],div[class*=banner]");
            for (Element div : adDivs) {
                div.remove();
                Log.d(TAG, "Removed div with ad class: " + div.className());
            }
            
            // Hapus div dengan ID yang mungkin terkait iklan
            Elements adIdDivs = doc.select("div[id*=iklan],div[id*=ads],div[id*=banner]");
            for (Element div : adIdDivs) {
                div.remove();
                Log.d(TAG, "Removed div with ad ID: " + div.id());
            }
            
            // Hapus elemen yang mengandung kata kunci konten iklan
            Elements allElements = doc.getAllElements();
            for (Element element : allElements) {
                String elementText = element.text().toLowerCase();
                String elementHtml = element.html().toLowerCase();
                
                for (String keyword : CONTENT_KEYWORDS) {
                    if ((elementText.contains(keyword) || elementHtml.contains(keyword)) && 
                            !element.tagName().equals("html") && 
                            !element.tagName().equals("body") && 
                            !element.tagName().equals("head")) {
                        element.remove();
                        Log.d(TAG, "Removed element containing keyword: " + keyword);
                        break;
                    }
                }
            }
            
            return doc.outerHtml();
        } catch (Exception e) {
            Log.e(TAG, "Error processing HTML content", e);
            return html;
        }
    }
    
    /**
     * Cek apakah AdBlocker sudah diinisialisasi
     */
    public static boolean isInitialized() {
        return isInitialized;
    }
} 
