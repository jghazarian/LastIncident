@file:OptIn(ExperimentalMaterial3Api::class)

package com.jghazarian.lastincident.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.jghazarian.lastincident.viewmodel.EntryUiState
import com.jghazarian.lastincident.IncidentDetails
import com.jghazarian.lastincident.util.convertMillisToDate
import com.jghazarian.lastincident.viewmodel.IncidentEntryViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentEntryCommonScreen(
    navigateBack: () -> Unit,
    viewModel: IncidentEntryViewModel = koinViewModel<IncidentEntryViewModel>()
) {
    val distinctTitles = viewModel.filteredTitles.collectAsStateWithLifecycle()
    viewModel.updateUiState(viewModel.entryUiState.incidentDetails)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Enter Your Incident")
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        IncidentEntryBody(
            entryUiState = viewModel.entryUiState,
            distinctTitles = distinctTitles.value,
            onIncidentValueChange = {
                viewModel.updateUiState(it)
            },
            onSaveClick = {
                viewModel.saveIncident()
                navigateBack()
            },
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
//                .windowInsetsPadding(WindowInsets.safeContent)
                .padding(
                    start = innerPadding.calculateStartPadding(_root_ide_package_.androidx.compose.ui.platform.LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(_root_ide_package_.androidx.compose.ui.platform.LocalLayoutDirection.current)
                ),
        )
    }
}

@Composable
fun IncidentEntryBody(
    entryUiState: EntryUiState,
    distinctTitles: List<String>,
    onIncidentValueChange: (IncidentDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
//        verticalArrangement = Arrangement.SpacedBy(20.dp)
    ) {
        IncidentInputForm(
            incidentDetails = entryUiState.incidentDetails,
            distinctTitles = distinctTitles,
            onValueChange = onIncidentValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = entryUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save")
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun IncidentInputForm(
    incidentDetails: IncidentDetails,
    distinctTitles: List<String>,
    modifier: Modifier = Modifier,
    onValueChange: (IncidentDetails) -> Unit = {},
    enabled: Boolean = true
) {
    val openDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }
    val datePickerState: DatePickerState = rememberDatePickerState()
    val timePickerState: TimePickerState = rememberTimePickerState(is24Hour = false)
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    // Up Icon when expanded and down icon when collapsed
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        modifier = Modifier,
    ) {
        Box {
            OutlinedTextField(
                value = incidentDetails.title,
                onValueChange = {
                    onValueChange(incidentDetails.copy(title = it))
                    expanded = true
                                },
                label = { Text("Incident Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()

                    }
                    .onFocusChanged {
                        if (it.isFocused) expanded = true
                    },
                enabled = enabled,
                singleLine = true,
                trailingIcon = {
                    Icon(icon, "drop down icon",
                        Modifier.clickable { expanded = !expanded })
                },
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false),
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                distinctTitles.forEach { title ->
                    DropdownMenuItem(
                        text = { Text(title) },
                        onClick = {
                        onValueChange(incidentDetails.copy(title = title))
                        expanded = false
                    })
                }
            }
        }

        OutlinedTextField(
            value = incidentDetails.content,
            onValueChange = { onValueChange(incidentDetails.copy(content = it)) },
            label = { Text("Incident Content") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = convertMillisToDate(incidentDetails.timeStamp),
            onValueChange = { Logger.d("Timestamp change: ${incidentDetails.timeStamp}") },
            label = { Text("Incident Timestamp") },
            modifier = Modifier.fillMaxWidth().clickable { openDialog.value = true },
            enabled = false,
            singleLine = true,
        )
    }

    IncidentDatePicker(
        datePickerState = datePickerState,
        openDialog = openDialog.value,
        onValueChange = {
            Logger.d("initial: ${convertMillisToDate(it ?: 0)} and just it: $it")
            onValueChange(incidentDetails.copy(timeStamp = it ?: 0))
            Logger.d("timestamp update: ${incidentDetails.timeStamp}")
            openTimeDialog.value = true
                        },
        onDismiss = { openDialog.value = false }
    )

    IncidentTimePicker(
        timePickerState = timePickerState,
        openDialog = openTimeDialog.value,
        onValueChange = {
            Logger.d("initial before time: ${convertMillisToDate(it ?: 0)} and just it: $it")
            val timestamp = datePickerState.selectedDateMillis ?: 0
            val timeWithZone = Instant.fromEpochMilliseconds(timestamp + it).toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            onValueChange(incidentDetails.copy(timeStamp = timeWithZone))
            Logger.d("timestamp update with time: ${incidentDetails.timeStamp}")
        },
        onDismiss = { openTimeDialog.value = false }
    )
}

@ExperimentalMaterial3Api
@Composable
fun IncidentDatePicker(
    datePickerState: DatePickerState = rememberDatePickerState(),
    openDialog: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (Long?) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (openDialog) {
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        onValueChange(datePickerState.selectedDateMillis)
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun IncidentTimePicker(
    timePickerState: TimePickerState = rememberTimePickerState(is24Hour = false),
    openDialog: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (Long) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (openDialog) {
        val confirmEnabled = remember {
            derivedStateOf { timePickerState.hour != 0 || timePickerState.minute != 0 }
        }
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) { Text("Cancel") }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        val millis = (timePickerState.hour * 3600000L) + (timePickerState.minute * 60000L)
                        onValueChange(millis)
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            text = {
                TimePicker(
                    state = timePickerState,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }
}