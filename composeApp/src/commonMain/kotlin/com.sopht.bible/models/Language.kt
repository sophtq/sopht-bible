package com.sopht.bible.models

import kotlinx.serialization.Serializable

@Serializable
data class Language(
    val id: Long,
    val name: String,
    val abbreviation: String,
)
