package com.example.schedule_bsuir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.schedule_bsuir.ui.theme.Schedule_bsuirTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Schedule_bsuirTheme {
                //getEmployees(this)
                //getDepartments(this)
                //getAuditories(this)

                //readEmployees(this)
                //readAuditories(this)

                getEmployeeSchedules(this)
            }
        }
    }
}

