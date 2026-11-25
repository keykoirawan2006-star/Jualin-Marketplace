package com.example.jualin_marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.jualin_marketplace.ui.MainApp
import com.example.jualin_marketplace.ui.theme.JualinMarketplaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JualinMarketplaceTheme {
                MainApp()
            }
        }
    }
}
