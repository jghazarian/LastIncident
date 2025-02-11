package com.jghazarian.lastincident

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.jghazarian.lastincident.theme.IncidentTheme

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

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}