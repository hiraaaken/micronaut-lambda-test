package com.example.notes

import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL
import java.time.LocalDateTime

@Singleton
class NoteRepository(private val dsl: DSLContext) {

  companion object {
    private val NOTES = DSL.table("notes")
    private val ID = DSL.field("id", Long::class.javaObjectType)
    private val CONTENT = DSL.field("content", String::class.java)
    private val CREATED_AT = DSL.field("created_at", LocalDateTime::class.java)
  }

  fun findAll(): List<Note> =
    dsl.select(ID, CONTENT, CREATED_AT)
      .from(NOTES)
      .orderBy(CREATED_AT.desc())
      .fetch { toNote(it) }

  fun findById(id: Long): Note? =
    dsl.select(ID, CONTENT, CREATED_AT)
      .from(NOTES)
      .where(ID.eq(id))
      .fetchOne { toNote(it) }

  fun create(content: String): Note =
    dsl.insertInto(NOTES)
      .columns(CONTENT)
      .values(content)
      .returning(ID, CONTENT, CREATED_AT)
      .fetchOne() !!
      .let { toNote(it) }

  fun update(id: Long, content: String): Note? =
    dsl.update(NOTES)
      .set(CONTENT, content)
      .where(ID.eq(id))
      .returning(ID, CONTENT, CREATED_AT)
      .fetchOne()
      ?.let { toNote(it) }
 
  fun delete(id: Long): Boolean =
    dsl.deleteFrom(NOTES)
      .where(ID.eq(id))
      .execute() > 0

  private fun toNote(record: Record) = Note(
    id = record[ID]!!,
    content = record[CONTENT]!!,
    createdAt = record[CREATED_AT]!!
  )
}
