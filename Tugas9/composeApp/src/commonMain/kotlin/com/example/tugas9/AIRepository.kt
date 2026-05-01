package com.example.tugas9

import kotlinx.coroutines.delay

class AIRepository(private val geminiService: GeminiService) {

    private val systemInstruction = "Kamu adalah penerjemah bahasa yang akurat."

    suspend fun translateText(text: String): Result<String> {
        return runCatching {
            if (text.isBlank()) throw Exception("Teks tidak boleh kosong.")

            val combinedPrompt = "$systemInstruction\nTerjemahkan teks ini: $text"
            val contents = listOf(Content(role = "user", parts = listOf(Part(text = combinedPrompt))))

            // Kita panggil service
            val response = geminiService.generateContent(contents).getOrGetNull()
                ?: throw Exception("Gagal terhubung ke server Google.")

            // --- PROTEKSI INDEX ERROR ---
            // Kita cek satu per satu secara bertahap, jangan langsung tembak index [0]
            val candidates = response.candidates
            if (candidates.isNullOrEmpty()) {
                throw Exception("Google tidak memberikan kandidat jawaban. (Cek Quota/Key)")
            }

            val firstCandidate = candidates[0]
            val content = firstCandidate.content
            val parts = content?.parts

            if (parts.isNullOrEmpty()) {
                // Jika teks kosong, kita cek alasannya
                val reason = firstCandidate.finishReason ?: "Tidak diketahui"
                throw Exception("Google berhenti memberikan jawaban. Alasan: $reason")
            }

            val botText = parts[0].text
            if (botText.isNullOrBlank()) {
                throw Exception("Google mengirim balasan tapi teksnya kosong.")
            }

            botText
        }
    }

    // Helper sederhana untuk menghindari loading lama saat error
    private fun <T> Result<T>.getOrGetNull(): T? = getOrNull()

    suspend fun sendMessage(history: List<ChatMessage>): Result<String> {
        val lastMessage = history.lastOrNull { it.isUser }?.text ?: ""
        return translateText(lastMessage)
    }
}