package com.jghazarian.lastincident.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

//TODO: make this koin friendly or remove if not needed
class TimerViewModel() : ViewModel() {
//    val currentMoment: Instant = kotlinx.datetime.Clock.System.now()
//    val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
//    val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
//    var time by remember { mutableStateOf("") }
    private val _timer = MutableStateFlow<String>("")
    val timer: StateFlow<String> = _timer

    private val _timeInMillis = MutableStateFlow<Long>(0)
    val timeInMillies: StateFlow<Long> = _timeInMillis

    init {
        viewModelScope.launch {
            while (isActive) {
                val currentMoment: Instant = kotlinx.datetime.Clock.System.now()
                val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
                _timer.value = "Current Time: ${datetimeInSystemZone.hour}:${datetimeInSystemZone.minute}:${datetimeInSystemZone.second}"
                _timeInMillis.value = currentMoment.epochSeconds
                delay(1000)
            }
        }
    }
}