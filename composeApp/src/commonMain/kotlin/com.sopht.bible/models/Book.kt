package com.sopht.bible.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName("id")
    val id: Long,
    @SerialName("language_id")
    val languageId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("abbreviation")
    val abbreviation: String,
    @SerialName("chapters_count")
    val chaptersCount: Long,
    @SerialName("verses_count")
    val versesCount: Long,
    @SerialName("testament")
    val testament: String,
    @SerialName("division")
    val division: String,
)
