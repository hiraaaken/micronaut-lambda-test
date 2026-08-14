package com.example.notes

import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
data class NoteResponse(
  val id: Long,
  val content: String,
  val createdAt: LocalDateTime
) {
  companion object {
    fun from(note: Note): NoteResponse {
      return NoteResponse(
        id = note.id,
        content = note.content,
        createdAt = note.createdAt
      )
    }
  }

}
