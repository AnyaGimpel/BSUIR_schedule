package com.example.schedule_bsuir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.schedule_bsuir.navigation.BottomNavigationBar
import com.example.schedule_bsuir.navigation.NavHostContainer
import com.example.schedule_bsuir.ui.theme.Schedule_bsuirTheme



class MainActivity : ComponentActivity() {
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
@Composable
fun SetupAppContent() {
    val navController = rememberNavController()
    Surface(color = Color.White) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            },
            content = { padding ->
                NavHostContainer(navController = navController, padding = padding)
            }
        )
    }
}


