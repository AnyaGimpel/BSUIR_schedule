package com.example.schedule_bsuir

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
import com.google.gson.reflect.TypeToken
import retrofit2.http.Path


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
    val studentGroupDto: StudentGroupDto,
    val schedules: Map<String, List<Schedule>>,
    val exams: List<Schedule>,
    val startDate: String,
    val endDate: String,
    val startExamsDate: String,
    val endExamsDate: String
)

data class StudentGroupDto(
    val specialityName: String,
    //val specialityCode: String,
    //val numberOfStudents: Int,
    val name: String,
    //val educationDegree: Int
)


fun getEmployeeSchedules(context: Context) {

    val employees_urlId = getEmployeesUrlId(context)
    Log.d("MainActivity", employees_urlId.toString())

    val auditories_full_name = getFulAudName(context)
    Log.d("MainActivity", auditories_full_name.toString())

    val gson = Gson()
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
                        allEmployeeSchedules.add(employeeSchedule)
                    }

                    if (employees_urlId.last() == urlId) { // Если это последний запрос
                       // val json = gson.toJson(allEmployeeSchedules)
                        val json = allEmployeeSchedules.joinToString(separator = "\n") { gson.toJson(it) }
                        val file = File(context.getExternalFilesDir(null), "employeeSchedule.json")
                        try {
                            file.writeText(json)
                            Log.d("MainActivity", "Данные успешно записаны в файл employeeSchedule.json")
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Ошибка при записи данных в файл: $e")
                        }
                    }
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<EmployeeSchedule>, t: Throwable) {
                // Обработка ошибки
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