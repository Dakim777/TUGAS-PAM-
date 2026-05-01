package com.example.tugas9

/**
 * Repository untuk mengelola logika bisnis AI.
 * Memenuhi kriteria Prompt Engineering (Well-designed system prompt).
 */
class AIRepository(private val geminiService: GeminiService) {
    
    // System Prompt yang dirancang untuk memberikan persona asisten yang cerdas.
    private val systemInstruction = """
        Kamu adalah asisten digital cerdas bernama 'Asisten AI'.
        Tujuanmu adalah membantu pengguna dengan informasi yang akurat, bermanfaat, dan ramah.
        
        Panduan Jawaban:
        1. Gunakan Bahasa Indonesia yang santun dan profesional.
        2. Jika pertanyaan tidak jelas, mintalah klarifikasi dengan sopan.
        3. Jika kamu tidak mengetahui jawaban pastinya, sampaikan dengan jujur.
        4. Berikan format jawaban yang terstruktur jika menjelaskan langkah-langkah.
    """.trimIndent()

    suspend fun sendMessage(history: List<ChatMessage>): Result<String> {
        return runCatching {
            // Validasi riwayat untuk memastikan struktur user-model bergantian (diwajibkan oleh Gemini)
            val validContents = mutableListOf<Content>()
            var lastRole: String? = null

            history.forEach { msg ->
                val currentRole = if (msg.isUser) "user" else "model"
                if (currentRole != lastRole) {
                    validContents.add(
                        Content(
                            parts = listOf(Part(text = msg.text)),
                            role = currentRole
                        )
                    )
                    lastRole = currentRole
                }
            }

            if (validContents.isEmpty()) {
                throw Exception("Input pesan tidak valid.")
            }

            val response = geminiService.generateContent(validContents, systemInstruction)
            
            // Mengambil teks dari kandidat pertama hasil generate
            val botResponse = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            
            botResponse ?: throw Exception("Maaf, terjadi kendala teknis dalam memproses jawaban.")
        }
    }
}
