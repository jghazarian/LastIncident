package com.jghazarian.lastincident

import co.touchlab.kermit.Logger

data class IncidentDetails(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
//    val timeStamp: String = "",
    val timeStamp: Long = 0,
)

fun IncidentDetails.toIncidentEntity(): IncidentEntity {
//    Logger.d("convert incident detail to entity timestamp: ${timeStamp} becomes: ${timeStamp.toLongOrNull()}")
    return IncidentEntity(
        id = id,
        title = title,
        content = content,
        timeStamp = timeStamp
//        timeStamp = timeStamp.toLongOrNull()
//            ?: 0   //TODO: this should probably default to a Time.Now()
    )
}

fun IncidentEntity.toIncidentDetails(): IncidentDetails = IncidentDetails(
    id = id,
    title = title,
    content = content,
    timeStamp = timeStamp
//    timeStamp = timeStamp.toString()
)