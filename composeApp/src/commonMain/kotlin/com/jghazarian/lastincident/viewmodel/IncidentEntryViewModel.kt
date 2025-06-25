package com.jghazarian.lastincident.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jghazarian.lastincident.IncidentDetails
import com.jghazarian.lastincident.database.IncidentEntity
import com.jghazarian.lastincident.repository.Repository
import com.jghazarian.lastincident.toIncidentDetails
import com.jghazarian.lastincident.toIncidentEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IncidentEntryViewModel : ViewModel(), KoinComponent {
    private val dataRepository: Repository by inject()

    //Default to current time for new incidents
    var entryUiState by mutableStateOf(EntryUiState(IncidentDetails(timeStamp = Clock.System.now().toEpochMilliseconds())))
        private set

    val distinctTitles: StateFlow<List<String>> =
        dataRepository.getDistinctTitles().map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = listOf()
            )

    private val filteredDistinctTitles = MutableStateFlow(listOf<String>())
    var filteredTitles : StateFlow<List<String>> = filteredDistinctTitles

    suspend fun filterTitles(input: String) {
        filteredDistinctTitles.value = dataRepository.getDistinctTitlesNoFlow().filter { title ->
                title.contains(input, ignoreCase = true)
            }
        }

    fun updateUiState(incidentDetails: IncidentDetails) {
        entryUiState = EntryUiState(incidentDetails = incidentDetails, isEntryValid = validateInput(incidentDetails))
        viewModelScope.launch {
            filterTitles(incidentDetails.title)
        }
    }

    fun saveIncident() {
        if (validateInput()) {
            viewModelScope.launch {
                dataRepository.addIncident(entryUiState.incidentDetails.toIncidentEntity())
            }
        }
    }

    private fun validateInput(uiState: IncidentDetails = entryUiState.incidentDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }
}

data class EntryUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val isEntryValid: Boolean = false,
    val incidentTitles: List<String> = listOf()
)

fun IncidentEntity.toEntryUiState(isEntryValid: Boolean = false): EntryUiState = EntryUiState(
    incidentDetails = this.toIncidentDetails(),
    isEntryValid = isEntryValid
)