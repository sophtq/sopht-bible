package com.sopht.bible.ui

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopht.bible.cache.AppDatabase
import com.sopht.bible.models.Verse
import com.sopht.bible.models.Version
import com.sopht.bible.repositories.BookRepository
import com.sopht.bible.repositories.ChapterRepository
import com.sopht.bible.repositories.LanguageRepository
import com.sopht.bible.repositories.VerseRepository
import com.sopht.bible.repositories.VersionRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

class BibleViewModel(httpClient: HttpClient, appDatabase: AppDatabase): ViewModel() {
//    private val _uiState = MutableStateFlow(OrderUiState(pickupOptions = pickupOptions()))
//    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    private val languageRepository = LanguageRepository(httpClient, appDatabase)
    private val versionRepository = VersionRepository(httpClient, appDatabase)
    private val bookRepository = BookRepository(httpClient, appDatabase)
    private val chapterRepository = ChapterRepository(httpClient, appDatabase)
    private val verseRepository = VerseRepository(appDatabase)

    fun initializeDatabase(onComplete: (Int) -> Unit) {
        viewModelScope.launch {
            val languages = languageRepository.getAllLanguagesFromServer()
            languages.fastForEach {
                languageRepository.insertLanguageIntoDB(it)
            }
            onComplete(1)
        }

        viewModelScope.launch {
            val versions = versionRepository.getAllVersionsFromServer()
            versions.fastForEach {
                versionRepository.insertVersionIntoDB(it)
            }
            onComplete(1)
        }

        viewModelScope.launch {
            val books = bookRepository.getAllBooksFromServer()
            books.fastForEach {
                bookRepository.insertBookIntoDB(it)
            }
            onComplete(1)
        }

        viewModelScope.launch {
            val chapters = chapterRepository.getAllChaptersFromServer()
            chapters.fastForEach {
                chapterRepository.insertChapterIntoDB(it)
            }
            onComplete(1)
        }
    }

    fun downloadVersion(version: Version, downloadProgress: (message: String, progress: Float) -> Unit, onComplete: () -> Unit) {
        viewModelScope.launch {
            val versesStringArray = versionRepository.downloadBible(version.acronym.lowercase()) { message, progress ->
                downloadProgress(
                    message,
                    progress
                )
            }
            downloadProgress("Download complete. Adding to database", 2F)
            val books = bookRepository.getBooksByLanguage(version.languageId)
//            downloadProgress("${books.size} books found")
            var verseStartIndex: Long = 0
            var verseEndIndex: Long
            books.forEach { book ->
                val chapters = chapterRepository.getChaptersForBook(book.id)
//                downloadProgress("${chapters.size} chapters found for ${book.id}")
                chapters.forEach { chapter ->
//                    downloadProgress("Adding to database...\n${book.abbreviation} ${chapter.id}")
                    verseEndIndex = verseStartIndex + chapter.verseCount
                    for (i in verseStartIndex until verseEndIndex  step 1) {
                        val verseId = verseRepository.constructVerseId(version.id, book.id, chapter.id, i+1)
                        var verse = Verse(verseId, book.id, book.name, chapter.id, i+1, version.acronym, version.id, versesStringArray[i.toInt()])
                        verse = verseRepository.deserializeVerseText(verse)
                        verseRepository.insertVerse(verse)
//                        val v = verseRepository.getVerse(verseId)
//                        println(v)
                    }
                    verseStartIndex = verseEndIndex
                    downloadProgress("Adding to database",verseStartIndex.toFloat() / versesStringArray.size.toFloat())
                }

            }
            if (verseStartIndex == versesStringArray.size.toLong()) {
                versionRepository.updateDownloadStatus(true, version.id)
                onComplete()
            }
        }
    }

    fun getVerses(): List<Verse> {
        return verseRepository.getNextVerses(1001001001, 1)
    }

    fun getVersions(): List<Version> {
        return versionRepository.getVersions()
    }

    fun getRandomVerse(): Verse {
        return verseRepository.getRandomVerse()
    }

    fun getLastMeal(): Verse {
        return verseRepository.getLastReadVerse()
    }

    fun getLastBookmarkedVerse(): Verse {
        return verseRepository.getLastBookmarkedVerse()
    }

    fun getVerse(id: Long): Verse {
        return verseRepository.getVerse(id)
    }
}