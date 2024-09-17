package com.sopht.bible

import androidx.compose.ui.window.ComposeUIViewController
import com.sopht.bible.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    MainApp()
}