package com.angkringan.tugas10.data

class ValidationException(message: String) : Exception(message)

class NoteValidator {
    fun isValid(note: Note): Boolean {
        return note.title.isNotEmpty() && note.title.length < 300
    }

    fun validate(note: Note) {
        if (note.title.isEmpty()) throw ValidationException("Title cannot be empty")
        if (note.title.length >= 300) throw ValidationException("Title too long")
    }
}