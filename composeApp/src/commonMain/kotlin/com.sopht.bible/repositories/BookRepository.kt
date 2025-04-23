package com.sopht.bible.repositories

import com.sopht.bible.cache.AppDatabase
import com.sopht.bible.models.Book
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

class BookRepository(private val httpClient: HttpClient, appDatabase: AppDatabase) {
    private val bookQueries = appDatabase.bookQueries

    fun getCount(): Long {
        return bookQueries.getCount().executeAsOne()
    }

    fun getBooksByLanguage(languageId: Long): List<Book> {
        return bookQueries.getBooksNamesByLanguage(languageId).executeAsList().map {
            Book(
                id = it.id,
                languageId = it.language_id,
                name = it.name,
                abbreviation = it.abbreviation,
                chaptersCount = it.chapters_count,
                versesCount = it.verses_count,
                testament = it.testament,
                division = it.division
            )
        }
    }

    fun insertBookIntoDB(book: Book): Boolean {
        return bookQueries.transactionWithResult {
            bookQueries.insertBook(
                id = book.id,
                name = book.name,
                abbreviation = book.abbreviation,
                languageId = book.languageId,
                chaptersCount = book.chaptersCount,
                versesCount = book.versesCount,
                testament = book.testament,
                division = book.division
            )
            bookQueries.lastInsertedBookRowId().executeAsOne() > 0L
        }
    }

    suspend fun getAllBooksFromServer(): List<Book> {
        return httpClient.get("https://sopht-bible-api.sophtq.com/books").body()
    }
}