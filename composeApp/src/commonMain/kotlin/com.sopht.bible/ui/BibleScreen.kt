package com.sopht.bible.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sopht.bible.models.Verse
import com.sopht.bible.utils.AppLogger
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun BibleScreen(vm: BibleViewModel) {
    var verses by remember { mutableStateOf(emptyList<Verse>()) }
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    scope.launch {
        verses = vm.getVerses()
    }

    LazyColumn (modifier = Modifier.fillMaxSize().height(100.dp).padding(16.dp), state = listState) {
        items(verses, key = { it.id }) { verse ->
            if (verse.verseNumber == 1L) {
                Column(verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(verse.bookName, modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.titleSmall)
                    Text(verse.chapterNumber.toString(), modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.headlineSmall)
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic)) {
                                append("${verse.verseNumber}. ")
                            }
                            append(verse.text)
                        },
                        modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic)) {
                            append("${verse.verseNumber}. ")
                        }
                        append(verse.text)
                    },
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    val buffer = 5

    // Derived state to determine when to load more items
    val shouldLoadNextVerses = remember {
        derivedStateOf {
            // Total number of items in the list
            val totalItemsCount = verses.size
            // Index of the last visible item
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Check if we have scrolled near the end
            verses.isNotEmpty() && verses.first().bookId != 66L && lastVisibleItemIndex >= (totalItemsCount - buffer)
        }
    }

    val shouldLoadPreviousVerses = remember {
        derivedStateOf {
            // Index of the first visible item
            val firstVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0

            verses.isNotEmpty() && !(verses.first().verseNumber == 1L && verses.first().chapterNumber == 1L && verses.first().bookId == 1L) && firstVisibleItemIndex <= buffer
        }
    }

    // Launch a coroutine when shouldLoadMore becomes true
    LaunchedEffect(shouldLoadNextVerses) {
        snapshotFlow { shouldLoadNextVerses.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                verses = loadNextVerses(vm, verses)
            }
    }

    LaunchedEffect(shouldLoadPreviousVerses) {
        snapshotFlow { shouldLoadPreviousVerses.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                verses = loadPreviousVerses(vm, verses)
            }
    }
}

fun loadNextVerses(vm: BibleViewModel, verses: List<Verse>): List<Verse> {
    val nextVerses = vm.getNextVerses(verses.last().id, verses.last().versionId)
    return verses + nextVerses
//    return if (verses.size > 200)
//        verses.subList(verses.lastIndex - 50, verses.lastIndex) + nextVerses
//    else
//        verses + nextVerses
}

fun loadPreviousVerses(vm: BibleViewModel, verses: List<Verse>): List<Verse> {
    val previousVerses = vm.getPreviousVerses(verses.first().id, verses.first().versionId)
    return previousVerses.reversed() + verses
//    return if (verses.size > 200)
//        previousVerses.reversed() + verses.subList(0, 50)
//    else
//        previousVerses.reversed() + verses
}