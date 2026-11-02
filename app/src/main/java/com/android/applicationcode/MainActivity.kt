package com.android.applicationcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.applicationcode.presentation.navigation.AppNavigation
import com.android.applicationcode.ui.theme.ApplicationCodeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApplicationCodeTheme {
                AppNavigation()
            }
        }
    }
}