package com.angkringan.tugas10.data

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NoteValidatorTest {
    private lateinit var validator: NoteValidator

    @BeforeTest
    fun setup() {
        validator = NoteValidator()
    }

    @Test
    fun test1_validNoteReturnsTrue() {
        val note = Note(1, "Shopping", "Buy milk")
        assertTrue(validator.isValid(note))
    }

    @Test
    fun test2_emptyTitleReturnsFalse() {
        val note = Note(1, "", "Content")
        assertFalse(validator.isValid(note))
    }

    @Test
    fun test3_titleTooLongReturnsFalse() {
        val note = Note(1, "a".repeat(300), "Content")
        assertFalse(validator.isValid(note))
    }

    @Test
    fun test4_validateEmptyTitleThrowsException() {
        assertFailsWith<ValidationException> {
            validator.validate(Note(1, "", "Content"))
        }
    }

    @Test
    fun test5_validateTitleTooLongThrowsException() {
        assertFailsWith<ValidationException> {
            validator.validate(Note(1, "a".repeat(300), "Content"))
        }
    }
}