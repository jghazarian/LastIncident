package com.jghazarian.lastincident.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jghazarian.lastincident.repository.DataRepository
import com.jghazarian.lastincident.IncidentDetails
import com.jghazarian.lastincident.repository.Repository
import com.jghazarian.lastincident.toIncidentDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//TODO: savedstatehandle might be good here to hold the incident id
class IncidentDetailViewModel : ViewModel(), KoinComponent {
    private val dataRepository: Repository by inject()

    fun getIncident(id: Long): StateFlow<DetailUiState> {
        return dataRepository.getIncident(id).map { DetailUiState(it.toIncidentDetails()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailUiState()
            )
    }
}

data class DetailUiState(
    val incidentDetails: IncidentDetails = IncidentDetails()
)

private const val TIMEOUT_MILLIS = 5_000L