package com.sopht.bible

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform