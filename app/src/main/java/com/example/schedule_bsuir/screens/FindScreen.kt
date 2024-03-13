package com.example.schedule_bsuir.screens


import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
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
    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("Не выбрано") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(30.dp).fillMaxWidth(),

            ) {

            Text(
                text = "Выбрать дату",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                        dateDialogState.show()
                }

            )

            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = formattedDate
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(20.dp).fillMaxWidth()
        ) {
            Text(
                text = "Выбрать пару",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    expandedDropdown = true // Устанавливаем значение true при клике
                }
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = selectedTime,
                color = Color.Black
            )

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),

            ) {
            ExposedDropdownMenuBox(
                items = listOf("Пара 1: 09:00 - 10:20", "Пара 2: 09:00 - 10:20", "Пара 3: 09:00 - 10:20"),
                onItemSelected = { selectedItem ->
                    expandedDropdown = false
                    selectedTime = selectedItem
                },
                expanded = expandedDropdown, // Передаем значение expandedDropdown
                selectedTime = selectedTime
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(20.dp).fillMaxWidth(),

            ) {
            androidx.compose.material3.Button(onClick = { /*TODO*/ }) {
                Text("Найти")
            }
        }

    }



    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
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

@Composable
fun ExposedDropdownMenuBox(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    expanded: Boolean,
    selectedTime: String
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { /* Ничего не делаем при закрытии */ },
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(onClick = {
                        onItemSelected(item)
                    }) {
                        Text(text = item)
                    }
                }
            }
        }

    }
}

