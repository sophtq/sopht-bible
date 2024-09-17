package com.sopht.bible.repositories

import com.sopht.bible.cache.AppDatabase
import com.sopht.bible.models.Language
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class LanguageRepository (private val httpClient: HttpClient, appDatabase: AppDatabase) {
//    private val httpClient = HttpClient {
//        install(ContentNegotiation) {
//            json(Json {
//                ignoreUnknownKeys = true
//                useAlternativeNames = false
//                prettyPrint = true
//                isLenient = true
//            })
//        }
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.ALL
//        }
//    }

    private val languageQueries = appDatabase.languageQueries

    fun getLanguages(bookId: Long): List<Language> {
        return languageQueries.getLanguages().executeAsList().map {
            Language(
                id = it.id,
                name = it.name,
                abbreviation = it.abbreviation
            )
        }
    }

    fun insertLanguageIntoDB(language: Language) {
        languageQueries.insertLanguage(language.id, language.name, language.abbreviation)
    }

    suspend fun getAllLanguagesFromServer(): List<Language> {
        return httpClient.get("https://sopht-bible-api.sophtq.com/languages").body()
    }
}