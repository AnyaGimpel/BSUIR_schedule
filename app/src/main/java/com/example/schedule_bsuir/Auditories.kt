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


interface AuditoriesAPI {
    @GET("/api/v1/auditories")
    fun getAuditories(): Call<Array<Auditories>>
}

data class Auditories(
    val id: Int,
    val name: String,
    val note: String?,
    val capacity: Int?,
    val auditoryType: AuditoryType,
    val buildingNumber: BuildingNumber,
    val department: DepartmentInfo
)

data class AuditoryType(
    val id: Int,
    val name: String,
    val abbrev: String
)

data class BuildingNumber(
    val id: Int,
    val name: String
)

data class DepartmentInfo(
    val idDepartment: Int,
    val abbrev: String,
    val name: String,
    val nameAndAbbrev: String
)

fun getAuditories(context: Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://iis.bsuir.by")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(AuditoriesAPI::class.java)

    api.getAuditories().enqueue(object : Callback<Array<Auditories>> {
        override fun onResponse(call: Call<Array<Auditories>>, response: Response<Array<Auditories>>) {
            if (response.isSuccessful) {
                val auditories = response.body()
                val filteredAuditories = auditories?.filter { it.department?.idDepartment == 20022 }

                // Преобразование в JSON
                val gson = Gson()
                val json = gson.toJson(filteredAuditories)

                // Сохранение JSON в файл
                val file = File(context.getExternalFilesDir(null), "auditories.json")
                try {
                    file.writeText(json)
                    Log.d("MainActivity", "Данные успешно записаны в файл auditories.json")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Ошибка при записи данных в файл: $e")
                }
            } else {
                // Обработка ошибки
            }

        }

        override fun onFailure(call: Call<Array<Auditories>>, t: Throwable) {
            // Обработка ошибки
        }
    })
}

fun readAuditories(context: Context) {
    val gson = Gson()
    val file = File(context.getExternalFilesDir(null), "auditories.json")
    Log.d("MainActivity", "Путь к файлу: ${file.absolutePath}")
    val jsonString = file.readText()
    val auditories_show = gson.fromJson(jsonString, Array<Auditories>::class.java)

    // Вывод данных в консоль
    for (auditories in auditories_show) {
        Log.d("MainActivity", "Имя аудитории: ${auditories.name}, Корпус: ${auditories.buildingNumber.name}, id кафедра: ${auditories.department.idDepartment}")
        // Добавьте другие свойства по необходимости
    }
}