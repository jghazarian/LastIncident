package com.jghazarian.lastincident

import com.jghazarian.lastincident.repository.DataRepository
import com.jghazarian.lastincident.repository.Repository
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() {
    startKoin {
        modules(commonModule(), appModule, platformModule())
    }
}

fun commonModule() = module {
    single { createJson() }
    single<Repository> { DataRepository() }
}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }