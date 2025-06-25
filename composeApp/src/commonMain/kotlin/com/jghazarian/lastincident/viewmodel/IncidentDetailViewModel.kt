package com.jghazarian.lastincident.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.jghazarian.lastincident.IncidentDetails
import com.jghazarian.lastincident.database.IncidentEntity
import com.jghazarian.lastincident.repository.Repository
import com.jghazarian.lastincident.toIncidentDetails
import com.jghazarian.lastincident.toIncidentEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IncidentDetailViewModel : ViewModel(), KoinComponent {
    private val savedStataHandle: SavedStateHandle by inject()
    private val dataRepository: Repository by inject()

    private val incidentId: StateFlow<Long> = savedStataHandle.getStateFlow("incidentId", -1L)

    //TODO: This file needs major cleanup. Currently there is a significant delay in showing the "time since" text when navigating to the detail screen and that feels like it should be faster.
    @OptIn(ExperimentalCoroutinesApi::class)
    val getIncidentDetailUiState: StateFlow<DetailUiState> =
//        savedStataHandle["incidentId"] = savedStataHandle.get<Long>("incidentId")
//        val id: Long = savedStataHandle["incidentId"]!!
//        Logger.d("Detail ui state id: ${incidentId.value}")
        incidentId.flatMapLatest { id ->
            if (id == -1L) {
                flowOf(DetailUiState(isLoading = false))
            } else {

                dataRepository.getIncident(incidentId.value)
                    .flatMapConcat { incident ->
                        Logger.d("incidentidx: ${incident.id}")
                        getRelatedIncidents(incident.title).map { relatedIncidents ->
                            DetailUiState(incident.toIncidentDetails(), relatedIncidents.filter { it.id != incidentId.value }, false)
                        }
                    }
//                    .stateIn(
//                        scope = viewModelScope,
//                        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                        initialValue = DetailUiState(IncidentDetails())
//                    )
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailUiState(IncidentDetails())
            )

    private fun getRelatedIncidents(title: String): Flow<List<IncidentEntity>> {
        return dataRepository.getIncidentsWithTitle(title)
    }

    fun deleteIncident() {
        viewModelScope.launch {
            dataRepository.deleteIncidentById(incidentId.value)
        }
    }

    fun updateIncidentId(incidentId: Long) {
        savedStataHandle["incidentId"] = incidentId
        Logger.d("incident id update: ${this.incidentId.value} from saved state: ${savedStataHandle.get<Long>("incidentId")}")
    }

    private val _periodSinceLast = MutableStateFlow<DateTimePeriod>(DateTimePeriod())
    val periodSinceLast: StateFlow<DateTimePeriod> = _periodSinceLast

    private fun timeSinceLastIncident(): DateTimePeriod {
//        savedStataHandle["incidentId"]
//        Logger.d("current id: ${incidentId.value} and saved sgate handle ${savedStataHandle.get<Long>("incidentId")}")
        Logger.d("test time since id: ${getIncidentDetailUiState.value.incidentDetails} and saved state id: ${incidentId.value}")
        val incidents = getIncidentDetailUiState.value.relatedIncidents + getIncidentDetailUiState.value.incidentDetails.toIncidentEntity()
//        val incidentTime = getIncidentDetailUiState.value.relatedIncidents
        val incidentTime = incidents.sortedBy { it.timeStamp }.reversed().firstOrNull()?.timeStamp
//        val incidentTime = homeUiState.value.incidents.sortedBy { it.timeStamp }.reversed().firstOrNull()?.timeStamp
//        Logger.d("incident time: $incidentTime")
        return if (incidentTime != null) {
            Instant.fromEpochMilliseconds(incidentTime).periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())
        } else {
            DateTimePeriod()
        }
    }

    private val _lastIncidentText = MutableStateFlow<String>("")
    val lastIncidentText: StateFlow<String> = _lastIncidentText

    init {
        viewModelScope.launch {
            while (isActive) {
                Logger.d("is loading? ${getIncidentDetailUiState.value.isLoading}")
                _periodSinceLast.value = timeSinceLastIncident()
                Logger.d("period: ${_periodSinceLast.value}")
                //Still trying to figure out why the DetailScreen is getting an initial uiState with id=0, causing bad info to come up initially for lastIncidentText.
                //This prevents that bad info from showing up for now.
                if (getIncidentDetailUiState.value.isLoading) {
                    _lastIncidentText.value = ""
                } else {
                    _lastIncidentText.value = (if (_periodSinceLast.value.years > 0) "${_periodSinceLast.value.years} years, " else "") +
                            (if (_periodSinceLast.value.months > 0) "${_periodSinceLast.value.months} months, " else "") +
                            (if (_periodSinceLast.value.days > 0) "${_periodSinceLast.value.days} days, " else "") +
                            (if (_periodSinceLast.value.hours > 0) "${_periodSinceLast.value.hours} hours, " else "") +
                            (if (_periodSinceLast.value.minutes > 0) "${_periodSinceLast.value.minutes} minutes, " else "") +
                            "${_periodSinceLast.value.seconds} seconds"
                }
                delay(1000)
            }
        }
    }
}

//Could make this parcelable to make things more robust
data class DetailUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val relatedIncidents: List<IncidentEntity> = listOf(),
    val isLoading: Boolean = true,
)

private const val TIMEOUT_MILLIS = 5_000L