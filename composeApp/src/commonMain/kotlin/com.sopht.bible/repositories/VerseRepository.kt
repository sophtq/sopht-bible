package com.sopht.bible.repositories

import com.sopht.bible.cache.AppDatabase
import com.sopht.bible.models.Verse
import io.ktor.client.HttpClient
import kotlin.collections.set

class VerseRepository (appDatabase: AppDatabase) {
    private val verseQueries = appDatabase.verseQueries

    fun constructVerseId(versionId: Long, bookId: Long, chapterId: Long, verseNo: Long): Long {
        val verseIdStr = versionId.toString().padStart(3,'0') +
                bookId.toString().padStart(3, '0') +
                chapterId.toString().padStart(3,'0') +
                verseNo.toString().padStart(3,'0')
        return verseIdStr.toLong();
    }

    fun getCount(): Long {
        return verseQueries.getCount().executeAsOne()
    }

    fun insertVerse(verse: Verse) {
        verseQueries.insertVerse(
            id = verse.id,
            bookId = verse.bookId,
            chapterNumber = verse.chapterNumber,
            verseNumber = verse.verseNumber,
            versionId = verse.versionId,
            verseText = verse.text,
            verseReferences = verse.references,
            title = verse.title,
            highlights = verse.highlights
        )
    }

    fun getVerse(id: Long): Verse {
        return verseQueries.getVerseById(id) { verseId, bookId, chapterNumber, verseNumber, versionID, verseText, title, bookmarkId, verseReferences, highlights, _, _, bookName, version ->
            Verse(
                id = verseId,
                bookId = bookId,
                bookName = bookName,
                chapterNumber = chapterNumber,
                verseNumber = verseNumber,
                versionId = versionID,
                bibleVersion = version,
                text = verseText,
                title = title,
                bookmarkId = bookmarkId,
                references = verseReferences ?: emptyMap(),
                highlights = highlights ?: emptyMap()
            )
        }.executeAsOne()
    }

    fun getLastReadVerse(): Verse? {
        return verseQueries.getLastViewedVerse { verseId, bookId, chapterNumber, verseNumber, versionID, verseText, title, bookmarkId, verseReferences, highlights, _, _, bookName, version ->
            Verse(
                id = verseId,
                bookId = bookId,
                bookName = bookName,
                chapterNumber = chapterNumber,
                verseNumber = verseNumber,
                versionId = versionID,
                bibleVersion = version,
                text = verseText,
                title = title,
                bookmarkId = bookmarkId,
                references = verseReferences ?: emptyMap(),
                highlights = highlights ?: emptyMap()
            )
        }.executeAsOneOrNull()
    }

    fun getRandomVerse(): Verse? {
        return verseQueries.getRandomVerse { verseId, bookId, chapterNumber, verseNumber, versionID, verseText, title, bookmarkId, verseReferences, highlights, _, _, bookName, version ->
            Verse(
                id = verseId,
                bookId = bookId,
                bookName = bookName,
                chapterNumber = chapterNumber,
                verseNumber = verseNumber,
                versionId = versionID,
                bibleVersion = version,
                text = verseText,
                title = title,
                bookmarkId = bookmarkId,
                references = verseReferences ?: emptyMap(),
                highlights = highlights ?: emptyMap()
            )
        }.executeAsOneOrNull()
    }

    fun getLastBookmarkedVerse(): Verse? {
        return verseQueries.getLastBookmarkedVerse { verseId, bookId, chapterNumber, verseNumber, versionID, verseText, title, bookmarkId, verseReferences, highlights, _, _, bookName, version ->
            Verse(
                id = verseId,
                bookId = bookId,
                bookName = bookName,
                chapterNumber = chapterNumber,
                verseNumber = verseNumber,
                versionId = versionID,
                bibleVersion = version,
                text = verseText,
                title = title,
                bookmarkId = bookmarkId,
                references = verseReferences ?: emptyMap(),
                highlights = highlights ?: emptyMap()
            )
        }.executeAsOneOrNull()
    }

    fun getNextVerses(lastVerseId: Long, versionId: Long): List<Verse> {
        return verseQueries.getNextVerses(versionId = versionId, id = lastVerseId) { verseId, bookId, chapterNumber, verseNumber, versionID, verseText, title, bookmarkId, verseReferences, highlights, _, _, bookName, version ->
            Verse(
                id = verseId,
                bookId = bookId,
                bookName = bookName,
                chapterNumber = chapterNumber,
                verseNumber = verseNumber,
                versionId = versionID,
                bibleVersion = version,
                text = verseText,
                title = title,
                bookmarkId = bookmarkId,
                references = verseReferences ?: emptyMap(),
                highlights = highlights ?: emptyMap()
            )
        }.executeAsList()
    }

    fun getPreviousVerses(firstVerseId: Long, versionId: Long): List<Verse> {
        return verseQueries.getPreviousVerses(versionId = versionId, id = firstVerseId)
        { verseId, bookId, chapterNumber, verseNumber, versionID, verseText, title, bookmarkId, verseReferences, highlights, _, _, bookName, version ->
            Verse(
                id = verseId,
                bookId = bookId,
                bookName = bookName,
                chapterNumber = chapterNumber,
                verseNumber = verseNumber,
                versionId = versionID,
                bibleVersion = version,
                text = verseText,
                title = title,
                bookmarkId = bookmarkId,
                references = verseReferences ?: emptyMap(),
                highlights = highlights ?: emptyMap()
            )
        }.executeAsList()
    }

    fun deserializeVerseText(oldVerse: Verse): Verse {
        var verseText = oldVerse.text.replace("<CL>", "\n")
            .replace("<CM>", "\n\n")
            .replace("<FI>", "[")
            .replace("<Fi>", "]")
            .replace("\n\n \n\n", "\n\n")
            .replace("\n\n\n\n", "\n\n")
        var verseTitle = ""
        while (verseText.contains("<TS>")) {
            if (verseTitle.isNotEmpty()) {
                verseTitle += "\n"
            }
            verseTitle += verseText.substringAfter("<TS>")
                .substringBefore("<Ts>")
            val titleStartPos = verseText.indexOf("<TS>")
            val titleEndPos = verseText.indexOf("<Ts>")+"<Ts>".length
            verseText = verseText.removeRange(titleStartPos, titleEndPos)
        }

        // Handle references extraction
        val verseReferences = mutableMapOf<IntRange, String>()
        var refPlaceHolder = "*"
        while (verseText.contains("<RF>")) {
            val reference = verseText.substringAfter("<RF>")
                .substringBefore("<Rf>")
            val refStartPos = verseText.indexOf("<RF>")
            val refEndPos = verseText.indexOf("<Rf>")+"<Rf>".length
            verseText = verseText.replaceRange(refStartPos, refEndPos, refPlaceHolder)
            verseReferences[IntRange(refStartPos, refEndPos)] = reference
            refPlaceHolder += "*"
        }

        // Handle Jesus's Words in Red
        var redText = ""
        val highlights = mutableMapOf<IntRange, String>()
        redText += verseText.substringAfter("<FR>")
            .substringBeforeLast("<Fr>")
        verseText = verseText.replace("<FR>$redText<Fr>", redText.replace("<Fr>", ""))
        redText = redText.replace("<Fr>", "")
        val redStartPos = verseText.indexOf(redText)
        val redEndPos = verseText.length - 1
        highlights[IntRange(redStartPos, redEndPos)] = "ff0000"

        return  Verse(
            id = oldVerse.id,
            bookId = oldVerse.bookId,
            bookName = oldVerse.bookName,
            chapterNumber = oldVerse.chapterNumber,
            verseNumber = oldVerse.verseNumber,
            versionId = oldVerse.versionId,
            bibleVersion = oldVerse.bibleVersion,
            text = verseText,
            title = verseTitle,
            bookmarkId = oldVerse.bookmarkId,
            references = verseReferences,
            highlights = highlights
        )
    }
}