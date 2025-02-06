package com.jghazarian.lastincident.di

import com.jghazarian.lastincident.repository.DataRepository
import com.jghazarian.lastincident.platformModule
import com.jghazarian.lastincident.repository.Repository
import kotlinx.serialization.json.Json
import org.koin.dsl.KoinAppDeclaration
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule(), platformModule())
    }

fun commonModule() = module {
    single { createJson() }
    single<Repository> { DataRepository() }
}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

//private const val TIMEOUT_MILLIS = 5_000L