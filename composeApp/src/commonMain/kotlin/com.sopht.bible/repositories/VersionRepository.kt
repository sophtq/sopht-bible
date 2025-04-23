package com.sopht.bible.repositories

import com.sopht.bible.cache.AppDatabase
import com.sopht.bible.models.Version
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class VersionRepository(private val httpClient: HttpClient, appDatabase: AppDatabase) {

    private val versionQueries = appDatabase.versionQueries

    fun getCount(): Long {
        return versionQueries.getCount().executeAsOne()
    }

    fun getVersions(): List<Version> {
        return versionQueries.getAllVersions().executeAsList().map {
            Version(
                id = it.id,
                languageId = it.language_id,
                name = it.name,
                acronym = it.acronym,
                description = it.description,
                isDownloaded = it.is_downloaded
            )
        }
    }

    fun getVersion(id: Long): Version {
        return versionQueries.getVersionById(id) {ID, name, acronym, description, language_id, is_downloaded, _, _ ->
            Version(
                ID, language_id, acronym, name, description, is_downloaded
            )
        }.executeAsOne()
    }

    fun getVersionsByDownloadStatus(isDownloaded: Boolean): List<Version> {
        return versionQueries.getVersionsByDownloadStatus(isDownloaded).executeAsList().map {
            Version(
                id = it.id,
                languageId = it.language_id,
                name = it.name,
                acronym = it.acronym,
                description = it.description,
                isDownloaded = it.is_downloaded
            )
        }
    }

    fun insertVersionIntoDB(version: Version) {
        versionQueries.insertVersion(
            id = version.id,
            name = version.name,
            acronym = version.acronym,
            languageId = version.languageId,
            description = version.description
        )
    }

    fun updateDownloadStatus(isDownloaded: Boolean, id: Long) {
        versionQueries.updateDownloadStatus(isDownloaded, id)
    }

    suspend fun getAllVersionsFromServer(): List<Version> {
        return httpClient.get("https://sopht-bible-api.sophtq.com/versions").body()
    }

    suspend fun downloadBible(versionAbbreviationLowerCase: String = "kjv", onDownloadProgress: (message: String, progress: Float) -> Unit): List<String> {
        val httpResponse: HttpResponse = httpClient.get("https://sopht-bible-api.sophtq.com/versions/download/$versionAbbreviationLowerCase") {
            onDownloadProgress("Downloading $versionAbbreviationLowerCase...", 0F)
            onDownload { bytesSentTotal, contentLength ->
                onDownloadProgress("Received $bytesSentTotal bytes from $contentLength",
                    bytesSentTotal.toFloat() / contentLength.toFloat()
                )
            }
        }
        val responseBody = httpResponse.bodyAsText()
        val versesStringArray = responseBody.split("\n\r")[0].split("\r\n")
        return versesStringArray
    }
}