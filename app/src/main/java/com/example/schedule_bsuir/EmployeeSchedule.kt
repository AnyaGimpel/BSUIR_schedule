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
    val photoLink: String,
    val calendarId: String,
    val id: Int,
    val urlId: String,
    val jobPositions: List<String>
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
    val specialityCode: String,
    val numberOfStudents: Int,
    val name: String,
    val educationDegree: Int
)


fun getEmployeeSchedules(context: Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://iis.bsuir.by/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(EmployeeScheduleAPI::class.java)

    api.getEmployeeSchedule("s-nesterenkov").enqueue(object : Callback<EmployeeSchedule> {
        override fun onResponse(call: Call<EmployeeSchedule>, response: Response<EmployeeSchedule>) {
            if (response.isSuccessful) {
                val employeeSchedule = response.body()

                val gson = Gson()
                val json = gson.toJson(employeeSchedule)

                val file = File(context.getExternalFilesDir(null), "employeeSchedule_s-nesterenkov.json")
                try {
                    file.writeText(json)
                    Log.d("MainActivity", "Данные успешно записаны в файл employeeSchedule_s-nesterenkov.json")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Ошибка при записи данных в файл: $e")
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