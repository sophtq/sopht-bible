package com.sopht.bible.utils

import java.util.logging.Level
import java.util.logging.Logger

actual object AppLogger {
    private val logger: Logger = Logger.getLogger(AppLogger::class.java.name)

    init {
        logger.level = Level.FINE
    }

    actual fun e(
        message: Any,
        throwable: Throwable?,
        tag: String
    ) {
        if (throwable != null) {
            logger.log(Level.SEVERE, "ERROR: [$tag] $message", throwable)
        } else {
            logger.severe("ERROR: [$tag] $message")
        }
    }

    actual fun w(message: Any, tag: String) {
        logger.warning("WARN: [$tag] $message")
    }

    actual fun d(message: String, tag: String) {
        logger.info("DEBUG: [$tag] $message")
    }

    actual fun i(message: String, tag: String) {
        logger.info("INFO: [$tag] $message")
    }
}