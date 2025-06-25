@file:OptIn(ExperimentalMaterial3Api::class)

package com.jghazarian.lastincident.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.jghazarian.lastincident.IncidentCard
import com.jghazarian.lastincident.theme.Typography
import com.jghazarian.lastincident.util.convertMillisToDate
import com.jghazarian.lastincident.viewmodel.DetailUiState
import com.jghazarian.lastincident.viewmodel.IncidentDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IncidentDetailScreen(
    navigateBack: () -> Unit,
    navigateToIncidentDetail: (Long) -> Unit,
    incidentDetailViewModel: IncidentDetailViewModel = koinViewModel<IncidentDetailViewModel>()
) {
    val uiState by incidentDetailViewModel.getIncidentDetailUiState.collectAsStateWithLifecycle()
    Logger.d("uistate: ${uiState.incidentDetails}")
    Logger.d("related: ${uiState.relatedIncidents.firstOrNull()}")
    val incidentText by incidentDetailViewModel.lastIncidentText.collectAsStateWithLifecycle()

    val openDeleteDialog = remember { mutableStateOf(false) }

    when {
        openDeleteDialog.value -> {
            DeleteDialog(
                onDismissRequest = {
                    openDeleteDialog.value = false
                },
                onConfirm = {
                    incidentDetailViewModel.deleteIncident()
                    openDeleteDialog.value = false
                    navigateBack()
                }
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Incident Details")
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        openDeleteDialog.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete Incident"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        IncidentDetailBody(
            uiState,
            periodText = incidentText,
            onIncidentClick = {
                incidentDetailViewModel.updateIncidentId(it)
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
                )
        )
    }

}

@Composable
fun IncidentDetailBody(
    incidentDetailUiState: DetailUiState,
    periodText: String,
    onIncidentClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text("Title: ${incidentDetailUiState.incidentDetails.title}")
        Text("Details: ${incidentDetailUiState.incidentDetails.content}")
        Text("Date: ${convertMillisToDate(incidentDetailUiState.incidentDetails.timeStamp)}")

        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "Time since last incident",
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(periodText)

        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        Text("Related Incidents", style = Typography.titleLarge)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(
                items = incidentDetailUiState.relatedIncidents.sortedBy { it.timeStamp }.reversed(),
                key = { it.id }) { item ->
                IncidentCard(
                    item = item,
                    showDetails = true,
                    onIncidentClick = { onIncidentClick(item.id) }
                )
            }
        }
    }
}

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete Incident"
            )
        },
        title = { Text("Delete Incident")},
        text = { Text("Do you want to delete this incident?")},
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("No")
            }
        }
    )
}