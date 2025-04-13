# Panduan Instalasi Animasu Browser

## Persiapan

Sebelum menginstal Animasu Browser, pastikan Anda telah melakukan hal berikut:

1. **Aktifkan Sumber Tidak Dikenal**

   Untuk Android 7 dan di bawahnya:

   - Buka **Pengaturan**
   - Pilih **Keamanan**
   - Aktifkan opsi **Sumber tidak dikenal**

   Untuk Android 8 dan di atasnya:

   - Buka **Pengaturan**
   - Pilih **Aplikasi**
   - Pilih **Akses khusus** atau **Khusus**
   - Pilih **Instal aplikasi tidak dikenal**
   - Pilih aplikasi yang akan Anda gunakan untuk menginstal APK (misalnya File Manager, Chrome)
   - Aktifkan opsi untuk aplikasi tersebut

2. **Pastikan Koneksi Internet Stabil**

   Animasu Browser memerlukan koneksi internet untuk membuka website Animasu.

## Langkah Instalasi

### Metode 1: Menggunakan APK Debug (Direkomendasikan)

1. Salin file APK debug (`app/build/outputs/apk/debug/app-debug.apk`) ke perangkat Android Anda.

   - Gunakan kabel USB, atau
   - Kirim file melalui email, WhatsApp, Telegram, dsb.

2. Di perangkat Android, buka File Manager dan cari file APK yang sudah disalin.

3. Ketuk file APK tersebut untuk memulai instalasi.

4. Jika muncul peringatan keamanan, pilih **Tetap Instal** atau **Instal Saja**.

5. Tunggu proses instalasi selesai, lalu pilih **Buka** untuk menjalankan aplikasi.

### Metode 2: Menggunakan Android Studio

Jika Anda memiliki akses ke Android Studio dan perangkat Android terhubung:

1. Buka proyek ini di Android Studio.

2. Hubungkan perangkat Android melalui USB dan aktifkan Mode Pengembang.

3. Pilih perangkat dari dropdown menu perangkat.

4. Klik tombol **Run** (ikon segitiga hijau) untuk menginstal dan menjalankan aplikasi.

## Pemecahan Masalah

### Masalah: "Tampaknya paket tidak lengkap"

Jika Anda melihat pesan error ini, coba lakukan langkah berikut:

1. **Pastikan File APK Lengkap**

   - Periksa ukuran file APK, seharusnya sekitar 6.5 MB.
   - Coba unduh ulang file APK.

2. **Hapus Versi Sebelumnya**

   - Jika Anda pernah menginstal versi sebelumnya, hapus terlebih dahulu.
   - Buka **Pengaturan** > **Aplikasi** > **Animasu Browser** > **Hapus Instalasi**.

3. **Periksa Ruang Penyimpanan**

   - Pastikan perangkat memiliki cukup ruang penyimpanan (minimal 20MB kosong).

4. **Restart Perangkat**

   - Restart perangkat Android Anda dan coba instal ulang.

5. **Gunakan File Manager Berbeda**
   - Beberapa file manager bisa menangani instalasi APK lebih baik dari yang lain.
   - Coba gunakan aplikasi seperti Solid Explorer, ES File Explorer, atau file manager bawaan.

### Masalah: "Aplikasi tidak diinstal"

Jika Anda melihat pesan ini:

1. **Versi Android Terlalu Rendah**

   - Pastikan perangkat Anda menggunakan Android minimal versi 7.0 (API level 24).

2. **Konflik Dengan Aplikasi Lain**

   - Periksa apakah Anda memiliki aplikasi dengan ID paket yang sama.
   - Hapus aplikasi tersebut terlebih dahulu.

3. **Play Protect**
   - Jika Google Play Protect memblokir instalasi, pilih "Instal Saja".

## Setelah Instalasi

Setelah berhasil diinstal, Animasu Browser akan:

1. Menampilkan splash screen selama beberapa detik.
2. Secara otomatis membuka website https://v9.animasu.cc/
3. Menghapus iklan-iklan tidak senonoh dan berbagai elemen iklan lainnya.

Selamat menikmati pengalaman nonton anime tanpa gangguan iklan!
