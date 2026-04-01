package com.skyrik.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.skyrik.app.navigation.SkyrikNavHost
import com.skyrik.core.ui.theme.SkyrikTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-activity entry point for Skyrik.
 * Edge-to-edge display is enabled for immersive map and tracking screens.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkyrikTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SkyrikNavHost()
                }
            }
        }
    }
}
