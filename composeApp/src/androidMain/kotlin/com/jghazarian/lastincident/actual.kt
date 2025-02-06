package com.jghazarian.lastincident

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jghazarian.lastincident.database.IncidentDataStore
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

actual fun platformModule() = module {
//    single { Android.create() }   //This was for a ktor client in fantasy
    single<IncidentDataStore> { createIncidentDataStore(get()) }
    single<AppDatabase> { createRoomDatabase(get()) }
}

//TODO: this is where fantasy league makes creators for datastore and database, but those already exist elsewhere, so hook those up
fun createIncidentDataStore(context: Context): IncidentDataStore {
    return IncidentDataStore {
        context.filesDir.resolve(
            "incident.json",
        ).absolutePath
    }
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