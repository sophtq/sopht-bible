package com.sopht.bible.models

data class BibleVersion (
    var id: Long,
    val fileName: String?,
    val lastModified: Long?,
    val bibleName: String?,
    val about: String?,
    val eolLength: Long?,
)