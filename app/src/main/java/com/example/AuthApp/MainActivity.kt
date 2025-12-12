package com.example.AuthApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.AuthApp.ui.theme.AuthApp_Theme
import com.example.AuthApp.auth.AuthApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthApp_Theme { AuthApp() }
        }
    }
}
