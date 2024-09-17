package com.sopht.bible.models

data class VerseDisplay(
    val mainVerse: Verse
) {
    override fun toString(): String {
        return mainVerse.toString()
    }
}
