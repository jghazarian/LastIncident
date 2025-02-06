package com.jghazarian.lastincident

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

//TODO: check and remove. I believe this predates use of Koin.
fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}