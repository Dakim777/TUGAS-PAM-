package com.example.tugas9

class AIRepository(private val geminiService: GeminiService) {
    private val systemPrompt = "Kamu adalah asisten cerdas untuk bisnis angkringan. Tugasmu membantu pemilik melacak status pembayaran dan merangkum pesanan harian. Jawab dalam Bahasa Indonesia yang ramah."

    suspend fun sendMessage(userPrompt: String): Result<String> {
        val fullPrompt = "$systemPrompt\n\nPertanyaan user: $userPrompt"
        return geminiService.generateContent(fullPrompt)
    }
}
