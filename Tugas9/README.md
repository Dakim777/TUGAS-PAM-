# AI Translator Pro - Kotlin Multiplatform

Aplikasi penerjemah cerdas berbasis Kotlin Multiplatform yang memanfaatkan kecanggihan Google Gemini AI untuk memberikan terjemahan yang akurat, natural, dan kontekstual.

## Fitur Ungkapan AI
- **Smart Translation**: Menerjemahkan teks antar bahasa secara otomatis dengan pemahaman konteks yang mendalam.
- **Auto Language Detection**: Mampu mendeteksi bahasa asal dan menerjemahkannya ke bahasa tujuan (Default: Indonesia <-> Inggris).
- **Contextual Accuracy**: Hasil terjemahan tidak hanya harfiah, tetapi menyesuaikan dengan nuansa budaya dan santun.

## Implementasi Rubrik Penilaian
1. **AI Integration (30%)**: Menggunakan `GeminiService` sebagai *service layer* yang terisolasi untuk menangani komunikasi asinkron dengan API Google Gemini melalui Ktor Client.
2. **Prompt Engineering (25%)**: Mengimplementasikan *system prompt* yang dirancang khusus di `AIRepository` untuk memaksa AI berperan sebagai penerjemah profesional tanpa melakukan percakapan di luar topik.
3. **Error Handling (20%)**: Penanganan error yang komprehensif termasuk deteksi API Key tidak valid, batas kuota (Rate Limit), dan kegagalan jaringan dengan mekanisme *exponential backoff retry* di `ChatViewModel`.
4. **UI/UX (15%)**: Antarmuka responsif menggunakan Jetpack Compose dengan *loading state* (Typing Indicator) dan skema warna yang nyaman di mata.
5. **Code Quality (10%)**: Mengikuti arsitektur MVVM (Model-View-ViewModel) yang bersih, pemisahan tanggung jawab yang jelas, dan penggunaan komponen modern (StateFlow, Coroutines).

## Cara Konfigurasi
1. Dapatkan API Key Gemini secara gratis di [Google AI Studio](https://aistudio.google.com/).
2. Tambahkan kunci tersebut ke dalam file `local.properties` di root project:
   ```properties
   API_KEY=AIzaSy...
   ```
3. Klik **Sync Project with Gradle Files** di Android Studio.
4. Jalankan aplikasi pada perangkat Android atau emulator.

---
*Proyek ini dikembangkan untuk memenuhi Tugas Integrasi AI pada mata kuliah Pemrograman Aplikasi Mobile.*
