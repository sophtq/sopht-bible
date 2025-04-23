package com.sopht.bible.utils

actual object AppLogger {
    actual fun e(message: Any, throwable: Throwable?, tag: String) {
        if (throwable != null) {
            println("ERROR: [$tag] $message. Throwable: ${throwable.message}")
        } else {
            println("ERROR: [$tag] $message")
        }
    }

    actual fun w(message: Any, tag: String) {
        println("WARN: [$tag] $message")
    }

    actual fun d(message: String, tag: String) {
        println("DEBUG: [$tag] $message")
    }

    actual fun i(message: String, tag: String) {
        println("INFO: [$tag] $message")
    }
}