package com.jghazarian.lastincident

import android.os.Build
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jghazarian.lastincident.di.AndroidApp
import com.jghazarian.lastincident.navigation.NavigationDestination
import com.jghazarian.lastincident.theme.IncidentTheme
import lastincident.composeapp.generated.resources.Res
import lastincident.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.insetsController?.show(WindowInsets.Type.statusBars())
//        ViewCompat.getWindowInsetsController(window.decorView)?.hide(WindowInsetsCompat.Type.systemBars())
//        window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.setDecorFitsSystemWindows(false)
//        } else {
//            // Show status bar
//            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE
//        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
//            App()
            IncidentTheme {
                IncidentApp()
            }
        }
    }
}



//@Composable
//fun BasicScreen(timerViewModel: TimerViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
//    val app = LocalContext.current.applicationContext as AndroidApp
//    val extras = remember(app) {
//        val container = app.container
//        MainViewModel.newCreationExtras(container)
//    }
//    val viewModel: MainViewModel = viewModel(
//        factory = MainViewModel.Factory,
//        extras = extras
//    )
//
//    val uiState by viewModel.homeUiState.collectAsState()
//
//    MaterialTheme {
////        val currentMoment: Instant = Clock.System.now()
////        val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
////        val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
//        val currentTime = timerViewModel.timer.collectAsState()
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                    val total: Int = uiState.incidents.size
//                    Text("items: $total")
////                    Text("Time: ${currentMoment.epochSeconds}")
//                    Text(currentTime.value)
////                    Text("Time Detail: ${datetimeInSystemZone.hour}:${datetimeInSystemZone.minute}:${datetimeInSystemZone.second}")
//                    Button(onClick = {
//                        viewModel.addItemToGroup(
//                            IncidentEntity(
//                                0,
//                                "test string${app.randomizer.nextInt()}",
//                                "more details",
//                                123
//                            )
//                        )
//                    }) {
//                        Text("Hiya")
//                    }
//
//                    LazyColumn {
//                        items(items = uiState.incidents, key = { it.id }) { item ->
//                            IncidentItem(
//                                item = item,
//                                onAddIncident = viewModel::addItemToGroup
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}


@Preview
@Composable
fun AppAndroidPreview() {
    App()
}