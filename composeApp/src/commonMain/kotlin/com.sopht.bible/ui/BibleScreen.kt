package com.sopht.bible.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopht.bible.models.Verse
import kotlinx.coroutines.launch

@Composable
fun BibleScreen(vm: BibleViewModel) {
    var verses by remember { mutableStateOf(emptyList<Verse>()) }
    val scope = rememberCoroutineScope()
    scope.launch {
        verses = vm.getVerses()
//        val rv = vm.getRandomVerse()
//        println(rv)
//        val v = vm.getVerse(1001001001)
//        println(v)
    }

    LazyColumn (modifier = Modifier.fillMaxWidth().height(100.dp)) {
        println(verses.size)
        item { Text("Verses", modifier = Modifier.height(100.dp)) }
        items(verses) {verse ->
            Text(verse.text)
        }
    }
}