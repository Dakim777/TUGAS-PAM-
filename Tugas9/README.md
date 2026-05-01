# TUGAS 9 PAM - Integrasi AI API
**Nama:** Muhammad Daffa Hakim Matondang  
**NIM:** 123140002  
**Program Studi:** Teknik Informatika - Institut Teknologi Sumatera  
**Mata Kuliah:** Pengembangan Aplikasi Mobile (PAM)[cite: 1]

---

## Deskripsi Proyek
Proyek ini merupakan implementasi dari materi **Pertemuan 9** mengenai **Integrasi AI API** ke dalam aplikasi mobile berbasis Kotlin Multiplatform (KMP)[cite: 1]. Aplikasi ini bertajuk **AI Translator Pro**, sebuah alat penerjemah cerdas yang memanfaatkan Large Language Models (LLMs) untuk menghasilkan terjemahan yang kontekstual dan akurat[cite: 1].

---

## Learning Objectives (Capaian Pembelajaran)
Berdasarkan modul praktikum, proyek ini telah memenuhi target berikut[cite: 1]:
* Memahami cara kerja Large Language Models (LLMs) dalam mengolah bahasa manusia[cite: 1].
* Mengintegrasikan Google Gemini API ke dalam aplikasi KMP menggunakan Ktor Client[cite: 1].
* Menerapkan teknik Prompt Engineering yang efektif untuk hasil yang optimal[cite: 1].
* Membangun fitur AI-powered dengan antarmuka yang responsif[cite: 1].

---

## Implementasi & Rubrik Penilaian[cite: 1]

### 1. AI Integration (30%)[cite: 1]
* **Service Layer**: Menggunakan `GeminiService` dengan engine Ktor Client untuk melakukan POST request ke endpoint Google Generative Language[cite: 1].
* **Model Engine**: Menggunakan model Gemini 2.5 Flash Lite yang diakses melalui jalur API `v1beta` untuk performa tinggi[cite: 1].
* **Data DTO**: Implementasi data class `@Serializable` untuk menangani request dan response JSON secara otomatis[cite: 1].

### 2. Prompt Engineering (25%)[cite: 1]
* **System Prompt**: Mendefinisikan perilaku AI sebagai "Penerjemah Profesional" agar hasil tetap akurat[cite: 1].
* **Specific Instruction**: Memberikan batasan agar AI hanya menghasilkan teks terjemahan tanpa narasi tambahan[cite: 1].
* **Context Awareness**: Memastikan AI memahami konteks kata yang diterjemahkan melalui instruksi yang spesifik[cite: 1].

### 3. Error Handling (20%)[cite: 1]
* **HTTP Guard**: Mampu menangani status code seperti **404** (Not Found), **403** (Forbidden), dan **429** (Rate Limited)[cite: 1].
* **Resilient Logic**: Menggunakan `runCatching` dan pengecekan array `candidates` untuk mencegah crash jika respon kosong[cite: 1].
* **User Feedback**: Aplikasi menampilkan pesan error yang informatif langsung di UI jika terjadi masalah API atau jaringan[cite: 1].

### 4. UI/UX (15%)[cite: 1]
* **Typing Indicator**: Menampilkan animasi loading (dot animation) saat AI sedang memproses terjemahan[cite: 1].
* **Chat Interface**: Menggunakan komponen `ChatBubble` untuk membedakan pesan pengguna dan respon AI[cite: 1].
* **Responsive State**: Antarmuka tetap responsif dan memberikan feedback visual saat proses pengiriman data[cite: 1].

### 5. Code Quality (10%)[cite: 1]
* **Security**: API Key disimpan di `local.properties` dan diakses via `BuildConfig` agar tidak terekspos di repository[cite: 1].
* **Architecture**: Menerapkan pemisahan logika bisnis (Repository) dan UI (ViewModel) menggunakan pola arsitektur modern[cite: 1].

---

## Dokumentasi Tampilan Aplikasi

### 1. Integrasi Chat Berhasil
Menampilkan UI saat AI berhasil memberikan respon terjemahan secara akurat.
> ![Screenshot Chat Berhasil](chat_berhasil.png)

### 2. Proses Loading (Typing Indicator)
Menampilkan indikator animasi saat aplikasi sedang menunggu respon dari server Gemini API[cite: 1].
> ![Screenshot Loading AI](loading_chat.png)

### 3. Error Handling di Aplikasi
Menampilkan UI saat aplikasi berhasil menangkap pesan error dan menampilkannya kepada pengguna agar aplikasi tidak force close[cite: 1].
> ![Screenshot Error Handling](eror_handling.png)

---

## Catatan Pengembangan & Troubleshooting
Beberapa kendala teknis yang diselesaikan dalam pengerjaan Tugas 9 ini meliputi:
1. **Endpoint Alignment**: Menyesuaikan URL API ke jalur `v1beta` agar sesuai dengan model Gemini 2.5 Flash Lite[cite: 1].
2. **Access Fix**: Mengatasi error 403 (Permission Denied) dengan regenerasi API Key dan aktivasi layanan di Google Cloud Console[cite: 1].
3. **Invalidate Cache**: Mengatasi masalah variabel `BuildConfig` yang tidak terupdate dengan melakukan *Invalidate Caches & Restart* pada Android Studio.

---

## Cara Menjalankan
1. Tambahkan `API_KEY=YOUR_KEY` pada file `local.properties`[cite: 1].
2. Lakukan **Build > Clean Project**.
3. Jalankan aplikasi melalui emulator Android atau simulator iOS.

---
**© 2026 Muhammad Daffa Hakim Matondang - Tugas 9 PAM ITERA**