package com.sopht.bible

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady
import com.sopht.bible.di.initKoin

//@OptIn(ExperimentalComposeUiApi::class)
//fun main() {
//    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
//        var shouldShowSplashScreen by rememberSaveable { mutableStateOf(true) }
//        if (shouldShowSplashScreen) {
//            SplashScreen(onComplete = { shouldShowSplashScreen = false })
//        } else {
//            MainApp(SophtBibleSDK(DatabaseDriverFactory(this)))
//        }
////        App()
//    }
//}

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    console.log("Hi, there")
    onWasmReady {
        CanvasBasedWindow("ComposeTarget") {
            MainApp()
        }
    }
}