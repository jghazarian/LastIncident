package com.jghazarian.lastincident.navigation

import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.jghazarian.lastincident.theme.Typography
import lastincident.composeapp.generated.resources.Res
import lastincident.composeapp.generated.resources.back_button
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    titleSize: TextUnit = 22.sp
//    titleStyle: TextStyle = Typography.titleLarge
) {
//    val collapsed = 22
//    val expanded = 28
    //TODO: Should be able to do this on the style name instead of just sizing
//    val topAppBarTextSize = (collapsed + (expanded - collapsed)*(1-scrollBehavior.state.collapsedFraction)).sp

    LargeTopAppBar(
//        title = { Text(title, style = titleStyle) },
        title = { Text(title, fontSize = titleSize) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back_button)
                    )
                }
            }
        }
    )
}