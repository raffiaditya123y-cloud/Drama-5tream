# DramaStream Cloud v1.3

Template Android streaming drama berbasis Kotlin + Jetpack Compose + Media3 ExoPlayer.

## Fitur
- Home drama
- Search
- Detail & episode
- HLS `.m3u8` player
- Favorit lokal
- Splash/theme dasar
- GitHub Actions untuk build APK otomatis

## Build otomatis via GitHub

1. Buat repository baru di GitHub.
2. Upload **semua isi ZIP ini** ke repository (folder `.github` juga harus ikut).
3. Commit ke branch `main`.
4. Buka tab **Actions** → workflow **Build Android APK**.
5. Setelah selesai, buka hasil workflow dan download artifact:
   `DramaStream-v1.3-debug-apk`

Workflow juga bisa dijalankan manual dengan **Run workflow**.

## Build lokal

Gunakan JDK 17 + Android SDK 35:

```bash
./gradlew assembleDebug
```

APK:
`app/build/outputs/apk/debug/app-debug.apk`

## Catatan

URL video demo di aplikasi harus diganti dengan sumber video yang kamu punya hak untuk distribusikan.
