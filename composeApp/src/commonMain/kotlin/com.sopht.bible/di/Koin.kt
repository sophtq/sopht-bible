package com.sopht.bible.di

import app.cash.sqldelight.ColumnAdapter
import com.sopht.bible.cache.AppDatabase
import com.sopht.bible.ui.BibleViewModel
import comsophtbiblecache.Verse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            platformModule,
            coreModule
        )
    }
//fun initKoin(appModule: Module): KoinApplication {
//    val koinApplication = startKoin {
//        modules(
//            appModule,
//            platformModule,
//            coreModule
//        )
//    }
//
//    return koinApplication
//}
//fun appModule() = listOf(platformModule, coreModule)

val coreModule = module {

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
//                logger = object : Logger {
//                    override fun log(message: String) {
//                        println(message)
//                    }
//                }
            }
        }
    }

//    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<AppDatabase> {
        val mapOfIntRangeToStringAdapter = object : ColumnAdapter<Map<IntRange, String>, String> {
            override fun decode(databaseValue: String) =
                if (databaseValue.isEmpty()) {
                    mapOf()
                } else {
                    databaseValue.split(",").associate {
                        val (leftIntRangeString, rightString) = it.split("=")
                        val (startString, endString) = leftIntRangeString.split("..")
                        IntRange(startString.toInt(), endString.toInt()) to rightString
                    }
                }
            override fun encode(value: Map<IntRange, String>) = value.entries.joinToString(",")
        }

        AppDatabase(
            driver = get(),
            verseAdapter = Verse.Adapter(
                highlightsAdapter = mapOfIntRangeToStringAdapter,
                verse_referencesAdapter = mapOfIntRangeToStringAdapter
            )
        )
    }

    singleOf(::BibleViewModel)
}

//val ktorModule = module {
//    single {
//        HttpClient {
//            install(ContentNegotiation) {
//                json(
//                    Json {
//                        ignoreUnknownKeys = true
//                        prettyPrint = true
//                        isLenient = true
//                    }
//                )
//            }
////            install(Logging) {
////                logger = Logger.DEFAULT
////                level = LogLevel.ALL
////            }
//        }
//    }

//    single { "https://rickandmortyapi.com" }
//}

expect val platformModule: Module