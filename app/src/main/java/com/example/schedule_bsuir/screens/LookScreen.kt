package com.example.schedule_bsuir.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LookScreen(){
    val context = LocalContext.current
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
    var selectedAud by remember { mutableStateOf("Не выбрано") }
    var dropdownExpanded by remember { mutableStateOf(false) }

/*

    // Функция для чтения данных из JSON
    fun readEmployeeSchedule(context: Context): List<EmployeeSchedule> {
        val externalFilesDir = context.getExternalFilesDir(null)
        val jsonFile = File(externalFilesDir, "EmployeeSchedule.json")
        val jsonString = jsonFile.readText()

        val gson = Gson()
        return gson.fromJson(jsonString, Array<EmployeeSchedule>::class.java).toList()
    }

    var employeeSchedules by remember {
        mutableStateOf(emptyList<EmployeeSchedule>())
    }

    employeeSchedules = readEmployeeSchedule(context)

 */
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
                text = "Выбрать аудиторию",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    expandedDropdown = true // Устанавливаем значение true при клике
                }
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = selectedAud,
                color = Color.Black
            )

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),

            ) {
            ExposedDropdownMenuBox2(
                items = listOf("502","601","603","604","605","611","612","613","615"),
                onItemSelected = { selectedItem ->
                    expandedDropdown = false
                    selectedAud = selectedItem
                },
                expanded = expandedDropdown, // Передаем значение expandedDropdown
                selectedAud = selectedAud
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(20.dp).fillMaxWidth(),

            ) {
            Button(onClick = {
                /*
                val filteredSchedules = employeeSchedules.filter { schedule ->
                    schedule.schedules.any { (_, schedules) ->
                        schedules.any { schedule ->
                            schedule.auditories.contains("615")
                        }
                    }
                }

                // Вывод отфильтрованных данных
                filteredSchedules.forEach { schedule ->
                    Log.d("Filtered Schedule", schedule.toString())
                }

                 */
            }) {
                Text("Показать")
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
fun ExposedDropdownMenuBox2(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    expanded: Boolean,
    selectedAud: String
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

