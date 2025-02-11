package com.jghazarian.lastincident.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import kotlin.random.Random

class AndroidApp: Application() {
    lateinit var randomizer: Random //TODO: if I still want a universal random seed, it should probably be in a koin module

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@AndroidApp)
            modules(appModule)
        }
        randomizer = Random(123)
    }
}