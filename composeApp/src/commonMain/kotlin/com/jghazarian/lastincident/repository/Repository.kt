package com.jghazarian.lastincident.repository

import com.jghazarian.lastincident.database.IncidentEntity
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun addIncident(incidentEntity: IncidentEntity)
    fun getData(): Flow<List<IncidentEntity>>
    fun getDistinctTitles(): Flow<List<String>>
    suspend fun getDistinctTitlesNoFlow(): List<String>
    fun loadData(): Flow<List<IncidentEntity>>
    fun getIncidentsWithTitle(title: String): Flow<List<IncidentEntity>>
    fun getIncident(id: Long): Flow<IncidentEntity>
    suspend fun refreshData()
}