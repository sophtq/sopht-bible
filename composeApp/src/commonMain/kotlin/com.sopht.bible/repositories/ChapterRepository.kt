package com.sopht.bible.repositories

import com.sopht.bible.cache.AppDatabase
import com.sopht.bible.models.Chapter
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

class ChapterRepository (private val httpClient: HttpClient, appDatabase: AppDatabase) {
    private val chapterQueries = appDatabase.chapterQueries
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

    fun getCount(): Long {
        return chapterQueries.getCount().executeAsOne()
    }

    fun getChaptersForBook(bookId: Long): List<Chapter> {
        return chapterQueries.getChaptersForBook(bookId).executeAsList().map {
            Chapter(
                id = it.id,
                bookId = it.book_id,
                verseCount = it.verses_count
            )
        }
    }

    fun getChapter(id: Long): Chapter {
        return chapterQueries.getChapter(id) { chapterId, bookId, versesCount, _, _ ->
            Chapter(
                id = chapterId,
                bookId = bookId,
                verseCount = versesCount
            )
        }.executeAsOne()
    }

    fun insertChapterIntoDB(chapter: Chapter) {
        chapterQueries.insertChapter(chapter.id, chapter.bookId, chapter.verseCount)
    }

    suspend fun getAllChaptersFromServer(): List<Chapter> {
        return httpClient.get("https://sopht-bible-api.sophtq.com/chapters").body()
    }
}