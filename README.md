# Animasu Browser

## Deskripsi

Animasu Browser adalah aplikasi Android yang memberikan pengalaman browsing aman tanpa iklan senonoh dan iklan judi untuk situs [Animasu](https://v9.animasu.cc/). Aplikasi ini menggunakan WebView dengan adblocker kustom untuk memblokir iklan yang tidak pantas, memberikan pengalaman nonton anime yang lebih nyaman.

## Fitur

- 🚫 Memblokir iklan yang tidak sesuai (seperti iklan judi, togel, dll)
- 🛑 Menghapus elemen HTML yang mengandung konten iklan
- 🔒 Mencegah popup dan redirects yang tidak diinginkan
- 📱 Mengoptimalkan tampilan website untuk layar mobile
- 🔄 Pengalaman browsing responsif dengan progres loading
- 🌙 Tampilan dark mode pada splash screen

## Teknologi

- Java
- Android WebView
- JSoup untuk memproses HTML
- JSON untuk konfigurasi aturan pemblokiran

## Cara Penggunaan

### Instalasi APK Debug (Direkomendasikan)

1. Download file APK dari direktori `app/build/outputs/apk/debug/app-debug.apk`
2. Pindahkan APK ke perangkat Android Anda
3. Install APK tersebut (Anda perlu mengaktifkan "Install dari sumber tidak dikenal" di pengaturan)
4. Buka aplikasi Animasu Browser
5. Aplikasi akan membuka website https://v9.animasu.cc/ secara otomatis

### Jika Mengalami Masalah Instalasi

Jika Anda melihat pesan "Tampaknya paket tidak lengkap" saat menginstal, coba lakukan langkah berikut:

1. Pastikan Anda menggunakan APK debug (`app-debug.apk`) bukan APK release unsigned
2. Pastikan APK telah didownload secara lengkap (periksa ukuran file, seharusnya sekitar 6.5 MB)
3. Pastikan Android Anda mengizinkan instalasi dari sumber tidak dikenal
   - Buka **Pengaturan** > **Keamanan** > aktifkan "Sumber tidak dikenal"
   - Pada Android versi baru: **Pengaturan** > **Aplikasi** > **Akses khusus** > **Instal aplikasi tidak dikenal**
4. Coba restart perangkat Android Anda dan install ulang

## Informasi Teknis

Aplikasi ini menggunakan beberapa teknik untuk menghilangkan iklan:

1. Memblokir permintaan domain iklan seperti googlesyndication, doubleclick, dll
2. Menghapus elemen HTML dengan kelas "lmd-iklan" dan kelas iklan lainnya
3. Mendeteksi dan memblokir konten iklan berdasarkan kata kunci
4. Injeksi JavaScript untuk menghapus elemen iklan secara dinamis
5. Skrip deteksi iklan yang berjalan secara periodik

## Catatan

Aplikasi ini dikembangkan hanya untuk tujuan pendidikan dan untuk mendapatkan pengalaman browsing yang lebih nyaman. Aplikasi ini tidak dimaksudkan untuk melanggar hak cipta atau ketentuan layanan dari situs web yang diakses.
