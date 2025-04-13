# Panduan Instalasi Menggunakan ADB

Jika Anda masih mengalami masalah dengan instalasi APK melalui metode biasa, Anda dapat mencoba menggunakan ADB (Android Debug Bridge). Metode ini lebih teknis tetapi lebih andal.

## Persyaratan

1. Komputer dengan Windows, macOS, atau Linux
2. Android SDK atau minimal ADB saja (dapat diunduh secara terpisah)
3. Kabel USB
4. Mode Pengembang dan USB Debugging diaktifkan di perangkat Android

## Langkah 1: Instalasi ADB

### Windows

1. Unduh [ADB Platform Tools](https://developer.android.com/tools/releases/platform-tools) dari situs web Android Developer.
2. Ekstrak file zip ke folder yang mudah diakses, misalnya `C:\adb`.
3. (Opsional) Tambahkan folder ADB ke PATH environment variable.

### macOS / Linux

```bash
# Untuk macOS menggunakan Homebrew
brew install android-platform-tools

# Untuk Ubuntu/Debian
sudo apt-get install android-tools-adb
```

## Langkah 2: Siapkan Perangkat Android

1. Aktifkan Mode Pengembang:

   - Buka **Pengaturan** > **Tentang Ponsel**
   - Ketuk **Nomor Build** 7 kali berturut-turut
   - Akan muncul notifikasi "Anda sekarang adalah pengembang"

2. Aktifkan USB Debugging:

   - Kembali ke **Pengaturan**
   - Buka **Opsi Pengembang** (mungkin di bawah **Sistem**)
   - Aktifkan **USB Debugging**

3. Hubungkan perangkat ke komputer menggunakan kabel USB.

## Langkah 3: Instal APK Menggunakan ADB

1. Buka terminal (Command Prompt, PowerShell, Terminal):

```bash
# Cek apakah perangkat sudah terhubung
adb devices

# Jika muncul dialog di perangkat Android, pilih "Izinkan"
# Seharusnya terlihat seperti ini:
# List of devices attached
# XXXXXXXX    device
```

2. Instal APK debug:

```bash
# Arahkan ke direktori tempat Anda menyimpan file APK
# Lalu jalankan perintah berikut:
adb install -r app-debug.apk

# Jika APK ada di folder lain, gunakan path lengkap:
adb install -r "C:\Users\YourName\Download\app-debug.apk"

# Opsi -r akan menimpa versi aplikasi yang sudah ada jika ada
```

3. Verifikasi instalasi. Jika berhasil, akan muncul pesan:

```
Success
```

4. Jalankan aplikasi dari launcher Android.

## Pemecahan Masalah ADB

### Perangkat tidak terdeteksi

1. Coba ganti kabel USB
2. Pastikan "USB Debugging" diaktifkan
3. Pada beberapa perangkat, Anda perlu mengubah mode USB ke "File Transfer" atau "MTP"
4. Install driver USB yang benar untuk perangkat Anda
5. Coba port USB yang berbeda

### Instalasi gagal

Jika melihat error, coba gunakan opsi tambahan:

```bash
# Paksa instalasi bahkan jika verifikasi gagal
adb install -r -d app-debug.apk

# Skip verifikasi
adb install -r -t app-debug.apk
```

### Pesan "INSTALL_FAILED_UPDATE_INCOMPATIBLE"

Ini berarti aplikasi sudah diinstal tetapi ditandatangani dengan kunci yang berbeda:

```bash
# Hapus aplikasi terlebih dahulu
adb uninstall com.chandra.animasu

# Lalu install ulang
adb install app-debug.apk
```

## Pemantauan Aplikasi

Jika aplikasi terinstal tetapi ada masalah, Anda dapat melihat log:

```bash
# Filter log hanya untuk aplikasi Animasu
adb logcat | grep "animasu"
```

Ini akan membantu mengidentifikasi masalah yang mungkin terjadi saat aplikasi dijalankan.
