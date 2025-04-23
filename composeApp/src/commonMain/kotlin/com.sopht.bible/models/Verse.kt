package com.sopht.bible.models

data class Verse(
    /*
    * John 3:16
    * 1043003016
    *
    * */
    val id: Long,
    val bookId: Long,
    val bookName: String,
    val chapterNumber: Long,
    val verseNumber: Long,
    val bibleVersion: String,
    val versionId: Long,
    val text: String,
    val title: String? = "",
    val references: Map<IntRange, String> = emptyMap(),
    val bookmarkId: Long? = -1,
    val highlights: Map<IntRange, String> = emptyMap()
) {
    override fun toString(): String {
        return "$bookName $chapterNumber:$verseNumber ($bibleVersion) - $text"
    }
}