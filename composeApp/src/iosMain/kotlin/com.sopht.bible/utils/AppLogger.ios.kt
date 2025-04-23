package com.sopht.bible.utils

import platform.Foundation.NSLog

actual object AppLogger {
    actual fun e(message: Any, throwable: Throwable?, tag: String) {

        if (throwable != null) {
            NSLog("ERROR: [$tag] $message. Throwable: $throwable CAUSE ${throwable.cause}")
        } else {
            NSLog("ERROR: [$tag] $message")
        }
    }

    actual fun w(message: Any, tag: String) {
        NSLog("WARN: [$tag] $message")
    }

    actual fun d(message: String, tag: String) {
        NSLog("DEBUG: [$tag] $message")
    }

    actual fun i(message: String, tag: String) {
        NSLog("INFO: [$tag] $message")
    }

}