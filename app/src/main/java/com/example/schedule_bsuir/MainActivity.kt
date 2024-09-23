package com.example.schedule_bsuir

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.schedule_bsuir.screens.LookScreen
import com.example.schedule_bsuir.ui.theme.Schedule_bsuirTheme



class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = getColor(R.color.purple_700)
            window.navigationBarColor = getColor(R.color.white)
            Schedule_bsuirTheme {
                SetupAppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupAppContent() {
    Surface(color = Color.White) {
        LookScreen()
    }
}

