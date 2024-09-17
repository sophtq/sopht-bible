package com.sopht.bible.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Version(
    @SerialName("id")
    val id: Long,
    @SerialName("language_id")
    val languageId: Long,
    @SerialName("acronym")
    val acronym: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String?,
    @Transient
    var isDownloaded: Boolean = false
)