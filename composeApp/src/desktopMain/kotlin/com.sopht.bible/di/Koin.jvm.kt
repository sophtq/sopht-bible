package com.sopht.bible.di

import app.cash.sqldelight.db.SqlDriver
import com.sopht.bible.cache.DatabaseDriverFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        DatabaseDriverFactory().createDriver()
    }
}