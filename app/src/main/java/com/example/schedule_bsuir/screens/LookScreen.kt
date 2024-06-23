package com.example.schedule_bsuir.screens

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import com.example.schedule_bsuir.json.Employee
import com.example.schedule_bsuir.json.EmployeeSchedule
import com.example.schedule_bsuir.json.filterEmployeeSchedule
import com.example.schedule_bsuir.json.getWeekNumber
import com.example.schedule_bsuir.json.readEmployeeSchedule
import com.google.gson.Gson
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.io.File
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LookScreen(){
    val context = LocalContext.current
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedAud by remember { mutableStateOf("Не выбрано") }
    var expandedDropdown by remember { mutableStateOf(false) }
    val dateDialogState = rememberMaterialDialogState()

    var employeeSchedules by remember { mutableStateOf(emptyList<EmployeeSchedule>()) }
    var filteredSchedules by remember { mutableStateOf(emptyList<EmployeeSchedule>()) }

    val formattedDate by remember {
        derivedStateOf {
            // Форматирование выбранной даты для отображения
            pickedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru")))
        }
    }
    val formattedDate1 by remember {
        derivedStateOf {
            // Форматирование выбранной даты для использования в запросах
            pickedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru")))
        }
    }
    var selectedDayOfWeek by remember {
        mutableStateOf(
            SimpleDateFormat("EEEE", Locale("ru")).format(Date()).capitalize()
        )
    }


    LaunchedEffect(Unit) {
        employeeSchedules = readEmployeeSchedule(context)
    }


    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(),
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
            Text(text = formattedDate)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
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
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            ) {
            Button(onClick = {
                val audNumb = "$selectedAud-2 к."
                Log.d("audNumb", audNumb)
                val weekNumb = getWeekNumber(context, formattedDate1)
                if (weekNumb != null) {
                    Log.d("WeekNumber", "Номер недели для $formattedDate1: $weekNumb")

                    filteredSchedules = filterEmployeeSchedule(employeeSchedules, audNumb, weekNumb, selectedDayOfWeek)

                    /*
                    filteredSchedules.forEach { schedule ->
                        Log.d("Filtered Schedule", schedule.toString())
                    }
                    */
                } else {
                    Log.d("WeekNumber", "Номер недели для $formattedDate1 не найден.")
                }
            }) {
                Text("Показать")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
        ) {
            DisplayFilteredSchedules(filteredSchedules = filteredSchedules, context = context)
        }
    }

    // Обновление выбранного дня недели
    fun updateSelectedDayOfWeek(date: LocalDate) {
        val dayOfWeek = SimpleDateFormat("EEEE", Locale("ru")).format(java.sql.Date.valueOf(date.toString()))
        selectedDayOfWeek = dayOfWeek.capitalize()
    }

    // Отображение Material Dialog для выбора даты
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                updateSelectedDayOfWeek(pickedDate)
                Log.d("Filtered Schedule", selectedDayOfWeek)
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

@Composable
fun DisplayFilteredSchedules(filteredSchedules: List<EmployeeSchedule>,context: Context) {
    LazyColumn {
        items(filteredSchedules) { schedule ->
            Card(Modifier.padding(8.dp)) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .width(300.dp)) {
                    schedule.schedules.forEach { (key, value) ->
                        value.forEach { scheduleItem ->
                            Column{
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text ="${scheduleItem.startLessonTime}",
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text ="${scheduleItem.subject} (${scheduleItem.lessonTypeAbbrev})"
                                    )
                                }


                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()

                                ) {
                                    Text(
                                        text = "${scheduleItem.endLessonTime}",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                    val fio = getEmployeesFio(context, schedule.employeeDto.urlId )
                                    Text("$fio")
                                }

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val groupNames = scheduleItem.studentGroups.joinToString { it.name }
                                    Text("$groupNames")
                                }

                                scheduleItem.note?.let { note ->
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                                    ) {
                                        Text(
                                            text = note,
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getEmployeesFio(context: Context, urlId: String ): String?  {
    val gson = Gson()
    val file = File(context.getExternalFilesDir(null), "employees.json")
    val jsonString = file.readText()
    val employees: List<Employee> = gson.fromJson(jsonString, Array<Employee>::class.java).toList()


    val employee = employees.find { it.urlId == urlId }
    return employee?.let {
        "${it.lastName} ${it.firstName.first()}. ${it.middleName.first()}."
    }
}


