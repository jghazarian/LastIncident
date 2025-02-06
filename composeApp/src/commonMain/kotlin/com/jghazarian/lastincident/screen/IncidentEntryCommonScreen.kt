@file:OptIn(ExperimentalMaterial3Api::class)

package com.jghazarian.lastincident.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.jghazarian.lastincident.viewmodel.EntryUiState
import com.jghazarian.lastincident.IncidentDetails
import com.jghazarian.lastincident.util.convertMillisToDate
import com.jghazarian.lastincident.viewmodel.IncidentEntryViewModel
import kotlinx.coroutines.flow.Flow
//import com.kizitonwose.calendar.compose.CalendarState
//import com.kizitonwose.calendar.compose.HorizontalCalendar
//import com.kizitonwose.calendar.compose.rememberCalendarState
//import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentEntryCommonScreen(
    navigateBack: () -> Unit,
    viewModel: IncidentEntryViewModel = koinViewModel<IncidentEntryViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()
    val distinctTitles = viewModel.filteredTitles.collectAsStateWithLifecycle()
    coroutineScope.launch {
        viewModel.filterTitles("")
    }

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
                coroutineScope.launch {
                    viewModel.filterTitles(it.title)
                }
            },
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveIncident()
                    navigateBack()
                }
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
    val datePickerState: DatePickerState = rememberDatePickerState()
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
//            onValueChange(incidentDetails.copy(timeStamp = convertMillisToDate(it ?: 0)))
            Logger.d("timestamp update: ${incidentDetails.timeStamp}")
                        },
        onDismiss = { openDialog.value = false }
    )

    Button(onClick = {
        Logger.d("check update: ${incidentDetails.timeStamp}")
    }) {
        Text("test update")
    }
}

//fun convertMillisToDate(millis: Long): String {
//    val instant = Instant.fromEpochMilliseconds(millis)
//    val dateFormat = DateTimeComponents.Format {
//        year()
//        char('-')
//        monthNumber()
//        char('-')
//        dayOfMonth()
//
//        char(' ')
//
//        hour()
//        char(':')
//        minute()
//        char(':')
//        second()
//    }
//
//    return instant.format(dateFormat)   //TODO: this can be given an offset, but due to off by one errors with timezones, this is probably what we want
//}

//TODO: include a time picker for more accuracy
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