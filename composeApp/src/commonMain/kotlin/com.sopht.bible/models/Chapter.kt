package com.sopht.bible.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.Long

@Serializable
data class Chapter(
  val id: Long,
  @SerialName("book_id")
  val bookId: Long,
  @SerialName("verses_count")
  val verseCount: Long
)
