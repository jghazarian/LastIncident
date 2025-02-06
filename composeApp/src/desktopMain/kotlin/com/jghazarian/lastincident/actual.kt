package com.jghazarian.lastincident

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import java.io.File

actual fun platformModule() = module {
    single<AppDatabase> { createRoomDatabase() }
}

fun createRoomDatabase(): AppDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}