package com.example.schedule_bsuir

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.schedule_bsuir.json.getAuditories
import com.example.schedule_bsuir.json.getEmployeeSchedules
import com.example.schedule_bsuir.json.getEmployees
import com.example.schedule_bsuir.ui.theme.Schedule_bsuirTheme
import kotlinx.coroutines.*

class FirstActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Schedule_bsuirTheme {
                MainScreen()

            }
        }
    }
}

@Composable
fun NavigateButton() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }) {
        Text("Перейти на другую страницу", fontSize = 15.sp)
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Получаем список аудиторий
        getAuditories(context)
        // Получаем список сотрудников
        getEmployees(context)
        delay(1000)
        // Получаем расписания для сотрудников
        getEmployeeSchedules(context)
    }

    NavigateButton()
}
