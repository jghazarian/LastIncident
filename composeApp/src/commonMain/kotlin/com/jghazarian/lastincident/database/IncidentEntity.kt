package com.jghazarian.lastincident.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//This is the incident type stored in the database, use this for displaying data coming from storage
@Entity
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val timeStamp: Long
)