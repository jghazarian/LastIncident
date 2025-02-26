package com.jghazarian.lastincident.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jghazarian.lastincident.IncidentCard
import com.jghazarian.lastincident.database.IncidentEntity
import com.jghazarian.lastincident.navigation.IncidentTopAppBar
import com.jghazarian.lastincident.theme.IncidentTheme
import com.jghazarian.lastincident.theme.Typography
import com.jghazarian.lastincident.util.convertMillisToDate
import com.jghazarian.lastincident.viewmodel.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navigateToIncidentDetail: (Long) -> Unit,
    navigateToIncidentEntry: () -> Unit,
    modifier: Modifier = Modifier,
) {
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
//    val isCollapsed = remember { derivedStateOf { scrollBehavior.state.collapsedFraction > 0.5 } }
    //TODO: this should be changing between styles not size
//    val topAppBarTitleStyle = if (isCollapsed.value) Typography.titleSmall else Typography.titleLarge
    val collapsed = 22
    val expanded = 34
    val topAppBarTextSize = (collapsed + (expanded - collapsed) * (1 - scrollBehavior.state.collapsedFraction)).sp
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    val periodSinceLast = viewModel.periodSinceLast.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            IncidentTopAppBar(
                title = "Last Incident App",
                canNavigateBack = false,
                modifier = modifier,
                scrollBehavior = scrollBehavior,
                navigateUp = {},
                titleSize = topAppBarTextSize
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToIncidentEntry,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(Icons.Filled.Add, "Add Button")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .windowInsetsPadding(WindowInsets.safeContent)
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Filter Unique Titles", style = Typography.labelMedium, modifier = Modifier.padding(4.dp))
                    Switch(
                        checked = viewModel.filter,
                        onCheckedChange = viewModel::toggleFilter,
                        modifier = Modifier.padding(4.dp)
                    )
                }

                if (uiState.incidents.isNotEmpty()) {
                    Text(
                        "Time of last incident: ${
                            convertMillisToDate(uiState.incidents.sortedBy { it.timeStamp }
                                .reversed().first().timeStamp)
                        }"
                    )
                    Text(
                        text = "Time since last incident",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        "${periodSinceLast.value.days} days, ${periodSinceLast.value.hours} hours, ${periodSinceLast.value.minutes} minutes, ${periodSinceLast.value.seconds} seconds,"
                    )
                }

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(
                        items = uiState.incidents.sortedBy { it.timeStamp }.reversed(),
                        key = { it.id }) { item ->
                        IncidentCard(
                            item = item,
                            onIncidentClick = { navigateToIncidentDetail(it) },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }
}

//TODO: this does not work in commonMain under Android studio. Should be able to do previews within androidMain pacakge
@Preview
@Composable
fun MainScreenPreview() {
    IncidentTheme {
        MainScreen(
            navigateToIncidentDetail = {},
            navigateToIncidentEntry = {},
            modifier = Modifier
        )
    }
}