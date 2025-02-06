@file:OptIn(ExperimentalMaterial3Api::class)

package com.jghazarian.lastincident.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jghazarian.lastincident.util.convertMillisToDate
import com.jghazarian.lastincident.viewmodel.DetailUiState
import com.jghazarian.lastincident.viewmodel.IncidentDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IncidentDetailScreen(
    incidentId: Long,
    navigateBack: () -> Unit,
    incidentDetailViewModel: IncidentDetailViewModel = koinViewModel<IncidentDetailViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by incidentDetailViewModel.getIncident(incidentId).collectAsState()

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
        IncidentDetailBody(
            uiState,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(_root_ide_package_.androidx.compose.ui.platform.LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(_root_ide_package_.androidx.compose.ui.platform.LocalLayoutDirection.current)
                )
        )
    }

}

@Composable
fun IncidentDetailBody(
    incidentDetailUiState: DetailUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text("Title: ${incidentDetailUiState.incidentDetails.title}")
        Text("Details: ${incidentDetailUiState.incidentDetails.content}")
        Text("Date: ${convertMillisToDate(incidentDetailUiState.incidentDetails.timeStamp)}")
    }
}