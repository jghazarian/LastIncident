package com.jghazarian.lastincident

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.jghazarian.lastincident.viewmodel.TimerViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel

import lastincident.composeapp.generated.resources.Res
import lastincident.composeapp.generated.resources.compose_multiplatform

@Composable
fun Clock() {
    val currentMoment: Instant = Clock.System.now()
    val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
    val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
    var time by remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit) {
        while(isActive) {
            time = "Time Detail: ${datetimeInSystemZone.hour}:${datetimeInSystemZone.minute}:${datetimeInSystemZone.second}"
            delay(1000)
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(time, fontSize = 40.sp)
    }
}

@Composable
@Preview
fun App(timerViewModel: TimerViewModel = TimerViewModel()) {
    MaterialTheme {
//        val currentMoment: Instant = Clock.System.now()
//        val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
//        val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentTime = timerViewModel.timer.collectAsState()
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
//                    Text("Time: ${currentMoment.epochSeconds}")
                    Text(currentTime.value)
//                    Text("Time Detail: ${datetimeInSystemZone.hour}:${datetimeInSystemZone.minute}:${datetimeInSystemZone.second}")
                }
            }
        }
    }
}