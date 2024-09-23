package com.example.schedule_bsuir.json

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File
import com.google.gson.GsonBuilder
import retrofit2.http.Path
import java.io.IOException
import kotlin.collections.any



interface EmployeeScheduleAPI {
    @GET("employees/schedule/{urlId}")
    fun getEmployeeSchedule(@Path("urlId") urlId: String): Call<EmployeeSchedule>
}

data class EmployeeDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val degree: String,
    val degreeAbbrev: String,
    val rank: String,
    //val photoLink: String,
    //val calendarId: String,
    val id: Int,
    val urlId: String,
    //val jobPositions: List<String>
)

data class Schedule(
    val weekNumber: List<Int>,
    val studentGroups: List<StudentGroupDto>,
    val numSubgroup: Int,
    val auditories: List<String>,
    val startLessonTime: String,
    val endLessonTime: String,
    val subject: String,
    val subjectFullName: String,
    val note: String,
    val lessonTypeAbbrev: String,
    val dateLesson: String,
    val startLessonDate: String,
    val endLessonDate: String,
    val announcement: Boolean,
    val split: Boolean,
    val employees: List<EmployeeDto>
)

data class EmployeeSchedule(
    val employeeDto: EmployeeDto,
    //val studentGroupDto: StudentGroupDto,
    var schedules: Map<String, List<Schedule>>,
    var exams: List<Schedule>,
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?
)

data class StudentGroupDto(
    val specialityName: String,
    //val specialityCode: String,
    //val numberOfStudents: Int,
    val name: String,
    //val educationDegree: Int
)


fun getEmployeeSchedules(context: Context) {

    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    val employees_urlId = getEmployeesUrlId(context)
    //Log.d("MainActivity", employees_urlId.toString())

    val auditories_full_name = getFulAudName(context)
    //Log.d("MainActivity", auditories_full_name.toString())

    val retrofit = Retrofit.Builder()
        .baseUrl("https://iis.bsuir.by/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(EmployeeScheduleAPI::class.java)

    val allEmployeeSchedules = mutableListOf<EmployeeSchedule>()

    // Итерируемся по каждому urlId и отправляем запросы
    for (urlId in employees_urlId) {

        api.getEmployeeSchedule(urlId).enqueue(object : Callback<EmployeeSchedule> {
            override fun onResponse(call: Call<EmployeeSchedule>, response: Response<EmployeeSchedule>) {
                if (response.isSuccessful) {
                    val employeeSchedule = response.body()
                    if (employeeSchedule != null) {
                        Log.e("MainActivity", "Получено расписание для urlId: $urlId")
                        // Инициализируем schedules, если оно null
                        val schedules = employeeSchedule.schedules ?: emptyMap()
                        val exams = employeeSchedule.exams ?: emptyList()

                        // Фильтруем расписание по подходящим аудиториям
                        val filteredSchedules = schedules.mapValues { (_, schedules) ->
                            schedules.filter { schedule ->
                                schedule.auditories.any { it in auditories_full_name }
                            }
                        }

                        // Фильтруем экзамены по подходящим аудиториям
                        employeeSchedule.exams?.let { exams ->
                            employeeSchedule.exams = exams.filter { exam ->
                                exam.auditories != null && exam.auditories.any { it in auditories_full_name }
                            }
                        }

                        // Проверяем поля на пустоту и добавляем расписание преподавателя к общему списку
                        if (filteredSchedules.any { it.value.isNotEmpty() } || (employeeSchedule.exams != null && employeeSchedule.exams.isNotEmpty())) {

                            //Log.d("MainActivity", "Расписание и/или экзамены не пустые")

                            employeeSchedule.schedules = filteredSchedules
                            val filteredEmplSchedules = employeeSchedule.schedules.filterKeys { it.isNotEmpty() && employeeSchedule.schedules[it]!!.isNotEmpty() }
                            employeeSchedule.schedules = filteredEmplSchedules
                            allEmployeeSchedules.add(employeeSchedule)
                        }else {
                            //Log.d("MainActivity", "Все расписания и экзамены пустые")
                        }

                    } else {
                        //Log.e("MainActivity", "Получено пустое расписание для urlId: $urlId")
                    }

                    if (employees_urlId.last() == urlId && allEmployeeSchedules.isNotEmpty()) {

                        val json = gson.toJson(allEmployeeSchedules)
                        val file = File(context.getExternalFilesDir(null), "employeeSchedule.json")

                        // Проверка, создан ли файл перед записью
                        if (!file.exists()) {
                            try {
                                file.createNewFile()
                            } catch (e: IOException) {
                                Log.e("Write to json", "Ошибка при создании файла (расписание преподавателей): $e")
                                return
                            }
                        }

                        try {
                            file.writeText(json)
                            Log.d("MainActivity", "Данные успешно записаны в файл employeeSchedule.json")
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Ошибка при записи данных в файл: $e")
                        }

                    }
                } else {
                    Log.e("MainActivity", "Ошибка при выполнении запроса (расписание преподавателей): ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EmployeeSchedule>, t: Throwable) {
                Log.e("MainActivity", "Ошибка при выполнении запроса (расписание преподавателей): ${t.message}")
            }
        })
    }
}

fun getEmployeesUrlId(context: Context): List<String>  {
    val gson = Gson()
    val file = File(context.getExternalFilesDir(null), "employees.json")
    val jsonString = file.readText()
    val employees: List<Employee> = gson.fromJson(jsonString, Array<Employee>::class.java).toList()

    val employees_urlId = employees.map { it.urlId }
    return employees_urlId
}
fun getFulAudName(context: Context): List<String> {
    val gson = Gson()
    val file = File(context.getExternalFilesDir(null), "auditories.json")
    val jsonString = file.readText()
    val auditories: List<Auditories> = gson.fromJson(jsonString, Array<Auditories>::class.java).toList()

    val auditories_full_name = auditories.map { it.name + "-" + it.buildingNumber.name }
    return auditories_full_name
}

fun readEmployeeSchedule(context: Context): List<EmployeeSchedule> {
    val externalFilesDir = context.getExternalFilesDir(null)
    val jsonFile = File(externalFilesDir, "EmployeeSchedule.json")
    val jsonString = jsonFile.readText()

    val gson = Gson()
    return gson.fromJson(jsonString, Array<EmployeeSchedule>::class.java).toList()
}

fun filterEmployeeSchedule(employeeSchedules: List<EmployeeSchedule>, audNumb: String, weekNumb: Int, selectedDayOfWeek: String): List<EmployeeSchedule> {
    val filteredSchedules = employeeSchedules.flatMap { employeeSchedule ->
        employeeSchedule.schedules[selectedDayOfWeek]?.filter { schedule ->
            schedule.auditories.contains(audNumb) && schedule.weekNumber.contains(weekNumb)
        }?.map { filteredSchedule ->
            // Создаем отдельную запись для каждого расписания
            employeeSchedule.copy(
                schedules = mapOf(selectedDayOfWeek to listOf(filteredSchedule))
            )
        } ?: emptyList()
    }


    return filteredSchedules.sortedBy { schedule ->
        schedule.schedules[selectedDayOfWeek]?.firstOrNull()?.startLessonTime
    }
}
