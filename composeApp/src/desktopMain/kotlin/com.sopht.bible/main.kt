package com.sopht.bible

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sopht.bible.di.initKoin
import java.awt.Dimension

fun main() = application {
    initKoin()
    Window(onCloseRequest = ::exitApplication, title = "SophtBible") {
        LaunchedEffect(Unit) {
            window.size = Dimension(425,750)
        }
        MainApp()
    }
}