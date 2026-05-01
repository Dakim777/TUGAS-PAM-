package com.example.tugas9

import kotlinx.coroutines.delay

/**
 * Repository untuk mengelola fitur Penerjemah AI.
 * Memenuhi Rubrik: AI Integration & Prompt Engineering (Translation Feature).
 */
class AIRepository(private val geminiService: GeminiService) {

    private val systemInstruction = """
        Kamu adalah 'AI Translator Pro', asisten ahli penerjemah bahasa yang sangat akurat.
        Tugas utamamu:
        1. Menerjemahkan teks yang diberikan pengguna ke dalam bahasa yang mereka minta.
        2. Jika pengguna tidak menyebutkan bahasa tujuan, terjemahkan ke Bahasa Indonesia (jika teks asing) atau ke Bahasa Inggris (jika teks Indonesia).
        3. Berikan hasil terjemahan yang natural, santun, dan sesuai konteks budaya.
        4. Jika ada istilah teknis, berikan penjelasan singkat di bawah terjemahan jika perlu.
        5. Jangan melakukan percakapan lain selain urusan penerjemahan.
    """.trimIndent()

    // Fungsi retryWithBackoff yang sudah diperbaiki
    private suspend fun <T> retryWithBackoff(
        maxRetries: Int = 3,
        initialDelayMillis: Long = 1000L,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis

        // Loop maxRetries - 1 kali. Sisakan 1 kali untuk percobaan terakhir di bawah.
        repeat(maxRetries - 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                if (e is AIError.RateLimited) {
                    // Gunakan e.retryAfter sesuai penamaan dari modul
                    val retryAfterSeconds = e.retryAfter
                    val delayMillis = maxOf(retryAfterSeconds * 1000L, currentDelay)
                    println("DEBUG: Rate limited detected. Retrying in ${delayMillis / 1000}s (Attempt ${attempt + 1}/$maxRetries)...")
                    delay(delayMillis)
                    currentDelay *= 2 // Exponential backoff
                } else {
                    // Jika bukan karena Rate Limit, langsung lemparkan error ke UI
                    throw e
                }
            }
        }

        // Percobaan terakhir (Attempt 3). Jika gagal, biarkan melempar exception aslinya.
        return block()
    }

    suspend fun translateText(text: String): Result<String> {
        return runCatching {
            if (text.isBlank()) throw Exception("Teks terjemahan tidak boleh kosong.")

            val contents = listOf(
                Content(
                    role = "user",
                    parts = listOf(Part(text = text))
                )
            )

            val response = retryWithBackoff {
                // PENTING: Tambahkan .getOrThrow() jika service-mu mereturn Result<T>
                // Ini memaksa error (seperti 429 Rate Limit) terlempar agar ditangkap blok catch di atas.
                geminiService.generateContent(contents, systemInstruction).getOrThrow()
            }

            // Mengambil respons. Sesuaikan struktur ini jika struktur model DTO-mu sedikit berbeda.
            val botResponse = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            botResponse ?: throw Exception("Maaf, gagal menerjemahkan teks tersebut.")
        }
    }

    suspend fun sendMessage(history: List<ChatMessage>): Result<String> {
        val lastMessage = history.lastOrNull { it.isUser }?.text ?: ""
        return translateText(lastMessage)
    }
}