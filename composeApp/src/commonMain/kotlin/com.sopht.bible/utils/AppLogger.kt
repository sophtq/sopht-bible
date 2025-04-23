package com.sopht.bible.utils

expect object AppLogger {
    fun e(message: Any, throwable: Throwable? = null, tag: String = "DEBUG")
    fun w(message: Any, tag: String = "DEBUG")
    fun d(message: String, tag: String = "DEBUG")
    fun i(message: String, tag: String = "DEBUG")
}