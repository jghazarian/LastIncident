package com.jghazarian.lastincident.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jghazarian.lastincident.repository.DataRepository
import com.jghazarian.lastincident.IncidentEntity
import com.jghazarian.lastincident.repository.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

class MainViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    val homeUiState: StateFlow<HomeUiState> =
        repository.getData().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = HomeUiState()
            )

    private val _periodSinceLast = MutableStateFlow<DateTimePeriod>(DateTimePeriod())
    val periodSinceLast: StateFlow<DateTimePeriod> = _periodSinceLast

    fun addItemToGroup(incidentEntity: IncidentEntity) {
        viewModelScope.launch {
            repository.addIncident(incidentEntity)
        }
    }

    private fun timeSinceLastIncident(): DateTimePeriod {
        val incidentTime = homeUiState.value.incidents.sortedBy { it.timeStamp }.reversed().firstOrNull()?.timeStamp
        return if (incidentTime != null) {
            Instant.fromEpochMilliseconds(incidentTime).periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())
        } else {
            DateTimePeriod()
        }
    }

    init {
        viewModelScope.launch {
            while (isActive) {
                _periodSinceLast.value = timeSinceLastIncident()
                delay(1000)
            }
        }
    }

    //TODO: see if this gets in the way of koin injection
//    companion object {
//
//        val APP_CONTAINER_KEY = CreationExtras.Key<AppContainer>()
//
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val appContainer = this[APP_CONTAINER_KEY] as AppContainer
//                val repository = appContainer.dataRepository
//                MainViewModel(repository = repository)
//            }
//        }
//
//        /**
//         * Helper function to prepare CreationExtras.
//         *
//         * USAGE:
//         *
//         * val mainViewModel: MainViewModel = ViewModelProvider.create(
//         *  owner = this as ViewModelStoreOwner,
//         *  factory = MainViewModel.Factory,
//         *  extras = MainViewModel.newCreationExtras(appContainer),
//         * )[MainViewModel::class]
//         */
//        fun newCreationExtras(appContainer: AppContainer): CreationExtras =
//            MutableCreationExtras().apply {
//                set(APP_CONTAINER_KEY, appContainer)
//            }
//    }
}

data class HomeUiState(val incidents: List<IncidentEntity> = listOf())

//private const val TIMEOUT_MILLIS = 5_000L