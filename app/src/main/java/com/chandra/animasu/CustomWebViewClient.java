package com.chandra.animasu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomWebViewClient extends WebViewClient {
    private static final String TAG = "CustomWebViewClient";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String currentPageContent = "";
    private final Map<String, Boolean> processedUrls = new HashMap<>();
    private final Context context;

    public CustomWebViewClient(Context context) {
        this.context = context;
        
        // Inisialisasi AdBlocker jika belum dilakukan
        if (!AdBlocker.isInitialized()) {
            AdBlocker.initialize(context);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d(TAG, "Page loading started: " + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d(TAG, "Page loading finished: " + url);

        // Jalankan script JavaScript untuk menghapus elemen dengan kelas "lmd-iklan"
        String removeAdsScript = 
                "javascript:(function() {" +
                "   var elements = document.getElementsByClassName('lmd-iklan');" +
                "   while(elements.length > 0) {" +
                "       elements[0].parentNode.removeChild(elements[0]);" +
                "   }" +
                "   console.log('Removed ads elements with class lmd-iklan');" +
                "})()";
        
        view.evaluateJavascript(removeAdsScript, null);

        // Script untuk menghapus banner iklan yang umum ditemukan
        String removeBannersScript =
                "javascript:(function() {" +
                "   var banners = document.querySelectorAll('div[id*=\"banner\"], div[class*=\"banner\"], " +
                "       iframe[src*=\"doubleclick\"], iframe[src*=\"googlesyndication\"], " +
                "       div[class*=\"iklan\"], div[id*=\"iklan\"]');" +
                "   for(var i=0; i<banners.length; i++) {" +
                "       banners[i].style.display='none';" +
                "       if (banners[i].parentNode) {" +
                "           banners[i].parentNode.removeChild(banners[i]);" +
                "       }" +
                "   }" +
                "   console.log('Removed banners and iframe ads');" +
                "})()";
        
        view.evaluateJavascript(removeBannersScript, null);
        
        // Jalankan script untuk menghapus iklan berbasis judi secara spesifik
        String removeGamblingAdsScript =
                "javascript:(function() {" +
                "   var selectors = [" +
                "       '[id*=\"judi\"]', '[class*=\"judi\"]', " +
                "       '[id*=\"slot\"]', '[class*=\"slot\"]', " +
                "       '[id*=\"togel\"]', '[class*=\"togel\"]', " +
                "       '[id*=\"bet\"]', '[class*=\"bet\"]', " +
                "       '[id*=\"casino\"]', '[class*=\"casino\"]'" +
                "   ];" +
                "   selectors.forEach(function(selector) {" +
                "       var elements = document.querySelectorAll(selector);" +
                "       for(var i=0; i<elements.length; i++) {" +
                "           if (elements[i] && elements[i].parentNode) {" +
                "               elements[i].style.display='none';" +
                "               try {" +
                "                   elements[i].parentNode.removeChild(elements[i]);" +
                "               } catch(e) {}" +
                "           }" +
                "       }" +
                "   });" +
                "   console.log('Removed gambling ads');" +
                "})()";
        
        view.evaluateJavascript(removeGamblingAdsScript, null);
        
        // Jadwalkan penghapusan iklan secara periodik
        String periodicRemovalScript = 
                "javascript:(function() {" +
                "   var adRemovalInterval = setInterval(function() {" +
                "       document.querySelectorAll('.lmd-iklan, [id*=\"iklan\"], [class*=\"iklan\"], [class*=\"banner\"], [id*=\"banner\"], [id*=\"judi\"], [class*=\"judi\"]').forEach(function(elem) {" +
                "           if (elem && elem.parentNode) {" +
                "               elem.style.display='none';" +
                "               elem.parentNode.removeChild(elem);" +
                "           }" +
                "       });" +
                "   }, 1000);" +
                "   setTimeout(function() { clearInterval(adRemovalInterval); }, 10000);" +
                "})()";
        
        view.evaluateJavascript(periodicRemovalScript, null);
        
        // Blokir popup dan redirect
        String blockPopupsScript = 
                "javascript:(function() {" +
                "   // Save original window.open function" +
                "   var originalOpen = window.open;" +
                "   // Override window.open to block popup ads" +
                "   window.open = function(url, name, features) {" +
                "       // Check if URL contains ad patterns" +
                "       if (!url || url.includes('judi') || url.includes('bet') || " +
                "           url.includes('slot') || url.includes('casino') || " +
                "           url.includes('togel') || url.includes('poker') || " +
                "           url.includes('ads') || url.includes('banner') || " +
                "           url.includes('clicktrack') || url.includes('doubleclick') || " +
                "           url.includes('tracking') || url.includes('advert')) {" +
                "           console.log('Blocked popup: ' + url);" +
                "           return null;" +
                "       }" +
                "       // Allow legitimate popups" +
                "       return originalOpen(url, name, features);" +
                "   };" +
                "   // Disable common redirect techniques" +
                "   Object.defineProperty(window, 'onbeforeunload', {" +
                "       get: function() { return null; }," +
                "       set: function() {}" +
                "   });" +
                "   // Overwrite location change methods that might be used for redirects" +
                "   var locationHooks = ['assign', 'replace', 'reload'];" +
                "   locationHooks.forEach(function(method) {" +
                "       var original = window.location[method];" +
                "       window.location[method] = function(url) {" +
                "           // Only allow same domain redirects or trusted domains" +
                "           if (!url || (typeof url === 'string' && " +
                "               (url.includes('animasu.cc') || (!url.includes('://') && !url.startsWith('//')))))" +
                "           {" +
                "               original.apply(this, arguments);" +
                "           } else {" +
                "               console.log('Blocked redirect attempt: ' + url);" +
                "           }" +
                "       };" +
                "   });" +
                "   console.log('Anti-redirect and popup protection installed');" +
                "})()";

        view.evaluateJavascript(blockPopupsScript, null);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        
        // Blokir URL yang mengarah ke situs iklan atau judi
        if (url.contains("judi") || url.contains("slot") || 
            url.contains("togel") || url.contains("casino") || 
            url.contains("bet") || url.contains("poker") ||
            url.contains("doubleclick") || url.contains("googlesyndication") ||
            url.contains("ads") || url.contains("advert") ||
            url.contains("banner") || url.contains("clicktrack")) {
            
            Log.d(TAG, "Blocked navigation to ad URL: " + url);
            
            // Tampilkan halaman error dari assets
            view.loadUrl("file:///android_asset/html/error_page.html");
            return true; // Mencegah navigasi
        }
        
        // Hanya izinkan navigasi ke domain animasu.cc atau subdomain
        if (!url.contains("animasu.cc")) {
            Log.d(TAG, "Blocked navigation to external URL: " + url);
            
            // Tampilkan halaman error dari assets
            view.loadUrl("file:///android_asset/html/error_page.html");
            return true; // Mencegah navigasi ke domain eksternal
        }
        
        // Izinkan navigasi normal di dalam situs animasu.cc
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        
        // Blokir URL iklan yang sudah diketahui
        if (AdBlocker.isAd(url)) {
            return AdBlocker.createEmptyResource();
        }
        
        // Jika URL adalah halaman HTML utama, proses untuk menghapus iklan
        if (url.contains("animasu.cc") && 
                (url.endsWith(".html") || !url.contains(".") || url.endsWith("/") ||
                 request.getRequestHeaders().containsKey("Accept") && 
                 request.getRequestHeaders().get("Accept").contains("text/html"))) {
            
            // Check if we've already processed this URL to avoid reprocessing
            if (processedUrls.containsKey(url) && processedUrls.get(url)) {
                return super.shouldInterceptRequest(view, request);
            }
            
            processedUrls.put(url, true);
            
            try {
                // Pakai default loader untuk HTML normal
                return super.shouldInterceptRequest(view, request);
            } catch (Exception e) {
                Log.e(TAG, "Error intercepting request", e);
                return super.shouldInterceptRequest(view, request);
            }
        }
        
        return super.shouldInterceptRequest(view, request);
    }
} 
