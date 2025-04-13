package com.chandra.animasu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private static final String ANIMASU_URL = "https://v9.animasu.cc/";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Inisialisasi AdBlocker dengan aturan dari file JSON
        AdBlocker.initialize(this);
        
        // Set padding untuk system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi komponen UI
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        
        // Setup back navigation handler
        setupBackNavigation();
        
        // Konfigurasi WebView
        setupWebView();
        
        // Muat situs Animasu
        webView.loadUrl(ANIMASU_URL);
    }

    private void setupBackNavigation() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        // Aktifkan JavaScript untuk WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        
        // Set user agent untuk mendapatkan pengalaman desktop
        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent + " AnimasuApp");

        // Set WebViewClient kustom dengan adblocker
        webView.setWebViewClient(new CustomWebViewClient(this));
        
        // Set WebChromeClient untuk menangani progress bar
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                
                // Tampilkan progress bar selama loading
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                    
                    // Jalankan script untuk menghapus iklan setelah halaman dimuat sepenuhnya
                    removeAdsFromPage(view);
                }
            }
        });
    }
    
    private void removeAdsFromPage(WebView webView) {
        // Script untuk menghapus iklan dan mencegah munculnya kembali
        String script = 
                "javascript:(function() {" +
                "   // Fungsi untuk menghapus dan menyembunyikan elemen iklan" +
                "   function removeAds() {" +
                "       // Hapus elemen dengan class lmd-iklan" +
                "       var elements = document.getElementsByClassName('lmd-iklan');" +
                "       while(elements.length > 0) {" +
                "           elements[0].parentNode.removeChild(elements[0]);" +
                "       }" +
                "       " +
                "       // Hapus semua elemen yang terkait dengan iklan" +
                "       var selectors = [" +
                "           '.blox', '.kln'," + // Tambahan untuk blox dan kln
                "           '[id*=\"iklan\"]', '[class*=\"iklan\"]'," +
                "           '[class*=\"banner\"]', '[id*=\"banner\"]'," +
                "           'iframe[src*=\"doubleclick\"]', 'iframe[src*=\"googlesyndication\"]'," +
                "           '[id*=\"judi\"]', '[class*=\"judi\"]'," +
                "           '[id*=\"slot\"]', '[class*=\"slot\"]'," +
                "           '[id*=\"togel\"]', '[class*=\"togel\"]'," +
                "           '[id*=\"bet\"]', '[class*=\"bet\"]'," +
                "           '[id*=\"casino\"]', '[class*=\"casino\"]'," +
                "           '#floatcenter', '.floatcenter'," +
                "           '[id*=\"float\"]', '[class*=\"float\"]'" +
                "       ];" +
                "       " +
                "       selectors.forEach(function(selector) {" +
                "           document.querySelectorAll(selector).forEach(function(element) {" +
                "               if (element) {" +
                "                   // Set display none dan pointer-events none" +
                "                   element.style.cssText = 'display: none !important; pointer-events: none !important; visibility: hidden !important; opacity: 0 !important; height: 0 !important; width: 0 !important; position: absolute !important; z-index: -9999 !important;';" +
                "                   if (element.parentNode) {" +
                "                       element.parentNode.removeChild(element);" +
                "                   }" +
                "               }" +
                "           });" +
                "       });" +
                "   }" +
                "   " +
                "   // Fungsi untuk memastikan elemen tetap tersembunyi" +
                "   function keepHidden() {" +
                "       removeAds();" +
                "       requestAnimationFrame(keepHidden);" + // Loop menggunakan requestAnimationFrame
                "   }" +
                "   " +
                "   // Jalankan penghapusan iklan segera" +
                "   removeAds();" +
                "   " +
                "   // Mulai loop untuk tetap menyembunyikan" +
                "   keepHidden();" +
                "   " +
                "   // Tambahkan MutationObserver untuk mendeteksi dan menghapus iklan baru" +
                "   var observer = new MutationObserver(function(mutations) {" +
                "       mutations.forEach(function(mutation) {" +
                "           if (mutation.addedNodes && mutation.addedNodes.length > 0) {" +
                "               removeAds();" +
                "           }" +
                "       });" +
                "   });" +
                "   " +
                "   // Konfigurasi observer dengan opsi yang lebih lengkap" +
                "   observer.observe(document.body, {" +
                "       childList: true," +
                "       subtree: true," +
                "       attributes: true," +
                "       attributeFilter: ['style', 'class']" + // Pantau perubahan style dan class
                "   });" +
                "   " +
                "   // Override CSS untuk memastikan elemen tetap tersembunyi" +
                "   var style = document.createElement('style');" +
                "   style.textContent = `" +
                "       .blox, .kln { display: none !important; pointer-events: none !important; visibility: hidden !important; opacity: 0 !important; }" +
                "       [class*=\"iklan\"], [id*=\"iklan\"], .blox, .kln { height: 0 !important; width: 0 !important; position: absolute !important; z-index: -9999 !important; }" +
                "   `;" +
                "   document.head.appendChild(style);" +
                "   " +
                "   // Override fungsi yang biasa digunakan untuk menampilkan iklan" +
                "   window.addEventListener('DOMContentLoaded', function() {" +
                "       Object.defineProperties(window, {" +
                "           'floatcenter': {" +
                "               value: null," +
                "               writable: false," +
                "               configurable: false" +
                "           }," +
                "           'showFloatAd': {" +
                "               value: function() { return false; }," +
                "               writable: false," +
                "               configurable: false" +
                "           }," +
                "           'showAds': {" +
                "               value: function() { return false; }," +
                "               writable: false," +
                "               configurable: false" +
                "           }" +
                "       });" +
                "   });" +
                "   " +
                "   // Blokir script iklan yang mencoba dimuat" +
                "   var originalCreateElement = document.createElement;" +
                "   document.createElement = function(tagName) {" +
                "       var element = originalCreateElement.call(document, tagName);" +
                "       if (tagName.toLowerCase() === 'script') {" +
                "           var originalSetAttribute = element.setAttribute;" +
                "           element.setAttribute = function(name, value) {" +
                "               if (value && (value.includes('ads') || value.includes('iklan') || " +
                "                   value.includes('banner') || value.includes('float') || " +
                "                   value.includes('blox') || value.includes('kln'))) {" +
                "                   return;" +
                "               }" +
                "               originalSetAttribute.call(element, name, value);" +
                "           };" +
                "       }" +
                "       return element;" +
                "   };" +
                "   console.log('Enhanced ad blocking script installed successfully');" +
                "})()";
        
        webView.evaluateJavascript(script, null);
    }

    @Override
    protected void onDestroy() {
        // Bersihkan WebView
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}