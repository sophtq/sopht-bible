package com.sopht.bible.models

data class Bookmark(
    val id: Long,
    val highlighted: Long?,
    val categoryId: Long?,
    val book: Long?,
    val chapter: Long?,
    val verseStart: Long?,
    val verseEnd: Long?,
    val content: String?,
    val bible: String?,
    val bookmarkDate: String?,
)