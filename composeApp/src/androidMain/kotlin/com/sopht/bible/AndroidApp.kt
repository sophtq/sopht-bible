package com.sopht.bible

import android.app.Application
import com.sopht.bible.di.initKoin
import org.koin.android.ext.koin.androidContext

class AndroidApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@AndroidApp)
        }
    }
}