package com.example.notes

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Serdeable
data class NoteRequest(
    @field:NotBlank
    @field:Size(max = 1000)
    val content: String
)
