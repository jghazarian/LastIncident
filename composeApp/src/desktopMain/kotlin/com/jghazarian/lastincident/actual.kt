package com.jghazarian.lastincident

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jghazarian.lastincident.database.IncidentDataStore
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import java.io.File

actual fun platformModule() = module {
    single<IncidentDataStore> { createIncidentDataStore() }
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

fun createIncidentDataStore(): IncidentDataStore {
    //TODO: this needs to be updated with real code
    val dataFile = File(System.getProperty("java.io.tmpdir"), "inicident.json")
    return IncidentDataStore {
        dataFile.absolutePath
//        "incident.json" //TODO: !!!!!!!!!!!!!!!!!This was just picked at random to get it moving forward
    }
}