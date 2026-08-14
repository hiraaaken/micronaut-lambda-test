package com.example.notes

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Put
import jakarta.validation.Valid


@Controller("/notes")
open class NotesController(private val noteRepository: NoteRepository) {

    @Get
    fun getAllNotes(): List<NoteResponse> {
        return noteRepository.findAll().map { NoteResponse.from(it) }
    }

    @Get("/{id}")
    fun getNoteById(id: Long): HttpResponse<NoteResponse> {
        val note = noteRepository.findById(id) ?: return HttpResponse.notFound()
        return HttpResponse.ok(NoteResponse.from(note))
    }

    @Post
    open fun createNote(@Body @Valid noteRequest: NoteRequest): HttpResponse<NoteResponse> {
        val note = noteRepository.create(noteRequest.content)
        return HttpResponse.created(NoteResponse.from(note))
    }

    @Put("/{id}")
    open fun updateNote(id: Long, @Body @Valid noteRequest: NoteRequest): HttpResponse<NoteResponse> {
        val note = noteRepository.update(id, noteRequest.content) ?: return HttpResponse.notFound()
        return HttpResponse.ok(NoteResponse.from(note))
    }

    @Delete("/{id}")
    fun deleteNote(id: Long): HttpResponse<Unit> {
      return if(noteRepository.delete(id)) HttpResponse.noContent() else HttpResponse.notFound()
    }
}
