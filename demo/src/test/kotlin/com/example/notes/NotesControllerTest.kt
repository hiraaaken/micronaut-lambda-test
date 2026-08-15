package com.example.notes

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import jakarta.validation.ConstraintViolationException

@MicronautTest
class NotesControllerTest(private val notesController: NotesController): StringSpec({

  "creates, reads, updates and deletes a note" {
    // Create a new note
    val created = notesController.createNote(NoteRequest("first draft"))
    created.status shouldBe HttpStatus.CREATED
    val note = created.body.get()
    note.content shouldBe "first draft"

    // Read the note back
    val fetched = notesController.getNoteById(note.id)
    fetched.status shouldBe HttpStatus.OK
    fetched.body.get().content shouldBe "first draft"

   // Update the note
    val updated = notesController.updateNote(note.id, NoteRequest("edited"))
    updated.status shouldBe HttpStatus.OK
    updated.body.get().content shouldBe "edited"

    val list = notesController.getAllNotes()
    list.any { it.id == note.id } shouldBe true

    // Delete the note
    val deleted = notesController.deleteNote(note.id)
    deleted.status shouldBe HttpStatus.NO_CONTENT


    val afterDelete = notesController.getNoteById(note.id)
    afterDelete.status shouldBe HttpStatus.NOT_FOUND
  }
  
  "getting a note that does not exist returns 404" {
    val response = notesController.getNoteById(-1L)
    response.status shouldBe HttpStatus.NOT_FOUND
  }

  "creating a note with blank content fails validation" {
    shouldThrow<ConstraintViolationException> {
      notesController.createNote(NoteRequest(""))
    }
  }
})
