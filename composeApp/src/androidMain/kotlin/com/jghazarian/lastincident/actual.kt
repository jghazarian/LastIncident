package com.jghazarian.lastincident

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

actual fun platformModule() = module {
//    single { Android.create() }   //This was for a ktor client in fantasy
    single<AppDatabase> { createRoomDatabase(get()) }
}

fun createRoomDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath(dbFileName)
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath,
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}