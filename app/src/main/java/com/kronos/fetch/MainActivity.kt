package com.kronos.fetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.kronos.fetch.ui.screen.FetchTestScreen
import com.kronos.fetch.ui.theme.FetchTestTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      FetchTestTheme {
        FetchTestScreen(
          modifier = Modifier.fillMaxSize(),
        )
      }
    }
  }
}