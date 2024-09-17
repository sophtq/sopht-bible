package com.sopht.bible.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Document(
    @SerialName("id")
    val id: Int,
    @SerialName("language_id")
    val languageId: Int,
    @SerialName("language")
    val language: String,
    @SerialName("idx")
    val idx: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String
)
