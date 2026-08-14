package com.example.notes

import java.time.LocalDateTime

data class Note(
  val id: Long,
  val content: String,
  val createdAt: LocalDateTime
)
