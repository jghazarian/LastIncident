package com.jghazarian.lastincident

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

//TODO: check and remove. I believe this predates use of Koin.
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
}