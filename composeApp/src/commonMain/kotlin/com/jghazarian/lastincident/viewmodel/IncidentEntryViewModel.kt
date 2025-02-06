package com.jghazarian.lastincident.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jghazarian.lastincident.repository.DataRepository
import com.jghazarian.lastincident.IncidentDetails
import com.jghazarian.lastincident.IncidentEntity
import com.jghazarian.lastincident.repository.Repository
import com.jghazarian.lastincident.toIncidentDetails
import com.jghazarian.lastincident.toIncidentEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IncidentEntryViewModel : ViewModel(), KoinComponent {
    private val dataRepository: Repository by inject()


    var entryUiState by mutableStateOf(EntryUiState())
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
        entryUiState =
            EntryUiState(incidentDetails = incidentDetails, isEntryValid = validateInput(incidentDetails))
    }

    suspend fun saveIncident() {
        if (validateInput()) {
            dataRepository.addIncident(entryUiState.incidentDetails.toIncidentEntity())
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

//data class IncidentDetails(
//    val id: Long = 0,
//    val title: String = "",
//    val content: String = "",
//    val timeStamp: String = "",
//)

//fun IncidentDetails.toIncidentEntity(): IncidentEntity = IncidentEntity(
//    id = id,
//    title = title,
//    content = content,
//    timeStamp = timeStamp.toLongOrNull() ?: 0   //TODO: this should probably default to a Time.Now()
//)

fun IncidentEntity.toEntryUiState(isEntryValid: Boolean = false): EntryUiState = EntryUiState(
    incidentDetails = this.toIncidentDetails(),
    isEntryValid = isEntryValid
)

//fun IncidentEntity.toIncidentDetails(): IncidentDetails = IncidentDetails(
//    id = id,
//    title = title,
//    content = content,
//    timeStamp = timeStamp.toString()
//)