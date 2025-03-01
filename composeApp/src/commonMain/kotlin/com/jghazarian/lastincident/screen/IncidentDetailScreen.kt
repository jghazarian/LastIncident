@file:OptIn(ExperimentalMaterial3Api::class)

package com.jghazarian.lastincident.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jghazarian.lastincident.IncidentCard
import com.jghazarian.lastincident.theme.Typography
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
    val coroutineScope = rememberCoroutineScope()   //TODO: not used currently, but will likely be useful if editing of incident is added here
    val uiState by incidentDetailViewModel.getIncidentDetailUiState(incidentId).collectAsStateWithLifecycle()

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

        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        Text("Related Incidents", style = Typography.titleLarge)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(
                items = incidentDetailUiState.relatedIncidents.sortedBy { it.timeStamp }.reversed(),
                key = { it.id }) { item ->
                IncidentCard(
                    item = item,
                    showDetails = true,
                    onIncidentClick = {  }
                )
            }
        }
    }
}