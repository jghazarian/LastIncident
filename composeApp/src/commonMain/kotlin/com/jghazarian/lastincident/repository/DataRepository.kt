package com.jghazarian.lastincident.repository

import com.jghazarian.lastincident.AppDatabase
import com.jghazarian.lastincident.database.IncidentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DataRepository : Repository, KoinComponent {
    private val database: AppDatabase by inject()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override suspend fun addIncident(incidentEntity: IncidentEntity) {
        database.getDao().insert(incidentEntity)
    }

    override fun getData(): Flow<List<IncidentEntity>> {
        scope.launch {
            if (database.getDao().count() < 1) {
                refreshData()
            }
        }
        return loadData()
    }

    override fun getDistinctTitles(): Flow<List<String>> {
        return database.getDao().getUniqueTitlesFlow()
    }

    override suspend fun getDistinctTitlesNoFlow(): List<String> {
        return database.getDao().getUniqueTitles()
    }

    override fun loadData(): Flow<List<IncidentEntity>> {
        return database.getDao().getAllAsFlow()
    }

    override fun getIncidentsWithTitle(title: String): Flow<List<IncidentEntity>> {
        return database.getDao().getIncidentsWithTitle(title)
    }

    override fun getIncident(id: Long): Flow<IncidentEntity>{
        return database.getDao().getIncidentFlow(id)
    }

    override suspend fun refreshData() {
        //TODO: this would do an endpoint call to update the feed
    }
}