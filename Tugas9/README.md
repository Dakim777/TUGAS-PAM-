# Tugas 9 - Integrasi Gemini AI

Aplikasi asisten cerdas berbasis Kotlin Multiplatform yang mengintegrasikan Google Gemini AI.

## Fitur AI
- **Smart Chat Assistant**: Asisten digital yang ramah dan responsif untuk membantu menjawab berbagai pertanyaan pengguna.
- **Contextual Conversation**: AI mampu memahami konteks percakapan berdasarkan riwayat pesan sebelumnya.
- **System Prompt Engineering**: Menggunakan instruksi sistem yang dirancang khusus untuk memberikan persona asisten yang profesional.

## Rubrik Penilaian & Implementasi
1. **AI Integration (30%)**: Implementasi Ktor Client di `GeminiService` untuk memanggil API Gemini secara asinkron.
2. **Prompt Engineering (25%)**: Instruksi sistem (System Prompt) yang mendefinisikan persona asisten di `AIRepository`.
3. **Error Handling (20%)**: Penanganan error jaringan, kuota habis, dan kunci API tidak valid dengan logic retry otomatis (Exponential Backoff) di `ChatViewModel`.
4. **UI/UX (15%)**: Antarmuka responsif menggunakan Jetpack Compose, dilengkapi dengan *loading states* (Typing Indicator) dan pesan error yang informatif.
5. **Code Quality (10%)**: Arsitektur MVVM (Model-View-ViewModel) yang bersih dan terpisah antara UI, Repository, dan Service.

## Cara Konfigurasi
1. Dapatkan API Key dari [Google AI Studio](https://aistudio.google.com/).
2. Tambahkan API Key ke dalam file `local.properties` di root project:
   ```properties
   API_KEY=YOUR_GEMINI_API_KEY
   ```
3. Jalankan aplikasi melalui Android Studio.
