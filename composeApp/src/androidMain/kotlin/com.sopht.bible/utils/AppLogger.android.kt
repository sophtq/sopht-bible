package com.sopht.bible.utils

import android.util.Log

actual object AppLogger {

    actual fun e(message: Any, throwable: Throwable?, tag: String) {
        if (throwable != null) {
            Log.e(tag, message.toString(), throwable)
        } else {
            Log.e(tag, message.toString())
        }
    }

    actual fun w(message: Any, tag: String) {
        Log.w(tag, message.toString())
    }

    actual fun d(message: String, tag: String) {
        Log.d(tag, message)
    }

    actual fun i(message: String, tag: String) {
        Log.i(tag, message)
    }
}