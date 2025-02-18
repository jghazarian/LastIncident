package com.jghazarian.lastincident.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jghazarian.lastincident.IncidentDetails
import com.jghazarian.lastincident.IncidentEntity
import com.jghazarian.lastincident.repository.Repository
import com.jghazarian.lastincident.toIncidentDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//TODO: savedstatehandle might be good here to hold the incident id
class IncidentDetailViewModel : ViewModel(), KoinComponent {
    private val dataRepository: Repository by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getIncidentDetailUiState(id: Long): StateFlow<DetailUiState> {
        return dataRepository.getIncident(id)
            .flatMapConcat { incident ->
                getRelatedIncidents(incident.title).map { relatedIncidents ->
                    DetailUiState(incident.toIncidentDetails(), relatedIncidents.filter { it.id != id })
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailUiState()
            )
    }

    private fun getRelatedIncidents(title: String): Flow<List<IncidentEntity>> {
        return dataRepository.getIncidentsWithTitle(title)
    }
}

data class DetailUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val relatedIncidents: List<IncidentEntity> = listOf()
)

private const val TIMEOUT_MILLIS = 5_000L