package com.example.schedule_bsuir.screens


import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.DayOfWeek
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FindScreen() {
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMMM yyyy", Locale("ru"))
                .format(pickedDate)
        }
    }


    val dateDialogState = rememberMaterialDialogState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {

        androidx.compose.material3.Button(
            onClick = {
                dateDialogState.show()
            },
            modifier = Modifier.padding(top = 50.dp)
            ) {
            Text(text = "Выбрать дату", color = Color.Black) // Черный цвет текста кнопки
        }

        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = formattedDate,
            modifier = Modifier.padding(top = 50.dp)
        )

    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                /*Toast.makeText(
                    applicationContext,
                    "Clicked ok",
                    Toast.LENGTH_LONG
                ).show()

                 */
            }
            negativeButton(text = "Отменить")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Выбор даты",
            allowedDateValidator = { date ->
                date.isAfter(LocalDate.now()) && date.dayOfWeek != DayOfWeek.SUNDAY
            },
            locale = Locale("ru")
        ) {
            pickedDate = it
        }
    }

}