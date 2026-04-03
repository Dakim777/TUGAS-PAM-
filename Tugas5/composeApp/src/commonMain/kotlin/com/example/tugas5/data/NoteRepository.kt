package com.example.tugas5.data

import com.example.tugas5.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteRepository {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        _notes.value = listOf(
            Note("1", "Rencana Akhir Pekan", "Pergi ke pantai Parangtritis bersama keluarga dan menikmati matahari terbenam.", isFavorite = true, date = now),
            Note("2", "Daftar Belanja Mingguan", "Beli sayuran segar, buah-buahan, dada ayam, dan persediaan kopi untuk satu minggu.", isFavorite = false, date = now),
            Note("3", "Ide Aplikasi Baru", "Membangun aplikasi pelacak kebiasaan belajar yang terintegrasi dengan kalender akademik mahasiswa.", isFavorite = true, date = now),
            Note("4", "Catatan Kuliah PAM", "Pelajari lebih lanjut tentang State Management di Jetpack Compose dan integrasi Navigation Component.", isFavorite = false, date = now),
            Note("5", "Buku yang Harus Dibaca", "Clean Code oleh Robert C. Martin dan Atomic Habits oleh James Clear untuk meningkatkan produktivitas.", isFavorite = true, date = now),
            Note("6", "Persiapan Presentasi", "Siapkan slide untuk presentasi project akhir minggu depan. Fokus pada demo fitur utama.", isFavorite = false, date = now),
            Note("7", "Resep Masakan Baru", "Cara membuat Nasi Goreng Spesial dengan bumbu rahasia dari nenek. Jangan lupa tambahkan telur mata sapi.", isFavorite = true, date = now),
            Note("8", "Jadwal Olahraga", "Lari pagi setiap hari Selasa dan Kamis. Angkat beban setiap hari Sabtu untuk menjaga kebugaran.", isFavorite = false, date = now),
            Note("9", "Target Tabungan", "Sisihkan 20% dari pendapatan bulanan untuk dana darurat dan investasi jangka panjang.", isFavorite = true, date = now),
            Note("10", "Maintenance Laptop", "Bersihkan file cache, update OS ke versi terbaru, dan bersihkan debu di kipas pendingin.", isFavorite = false, date = now)
        )
    }

    fun getNoteById(id: String): Note? {
        return _notes.value.find { it.id == id }
    }

    fun addNote(title: String, content: String) {
        val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        val id = (now + (0..1000).random()).toString() // Sedikit variasi agar ID unik
        val newNote = Note(
            id = id,
            title = title,
            content = content,
            date = now
        )
        _notes.value = listOf(newNote) + _notes.value
    }

    fun updateNote(id: String, title: String, content: String) {
        _notes.value = _notes.value.map {
            if (it.id == id) it.copy(title = title, content = content) else it
        }
    }

    fun toggleFavorite(id: String) {
        _notes.value = _notes.value.map {
            if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun deleteNote(id: String) {
        _notes.value = _notes.value.filter { it.id != id }
    }
}
