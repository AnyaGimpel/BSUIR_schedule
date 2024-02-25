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

interface DepartmentsAPI {
    @GET("/api/v1/departments")
    fun getDepartments(): Call<Array<Departments>>
}

data class Departments(
    val id: Int,
    val name: String,
    val abbrev: String
)

fun getDepartments(context: Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://iis.bsuir.by")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(DepartmentsAPI::class.java)

    api.getDepartments().enqueue(object : Callback<Array<Departments>> {
        override fun onResponse(call: Call<Array<Departments>>, response: Response<Array<Departments>>) {
            if (response.isSuccessful) {
                val departments = response.body()

                // Преобразование в JSON
                val gson = Gson()
                val json = gson.toJson(departments)

                // Сохранение JSON в файл
                val file = File(context.getExternalFilesDir(null), "departments.json")
                try {
                    file.writeText(json)
                    Log.d("MainActivity", "Данные успешно записаны в файл departments.json")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Ошибка при записи данных в файл: $e")
                }
            } else {
                // Обработка ошибки
            }

        }

        override fun onFailure(call: Call<Array<Departments>>, t: Throwable) {
            // Обработка ошибки
        }
    })
}

fun readDepartments(context: Context) {
    val gson = Gson()
    val file = File(context.getExternalFilesDir(null), "departments.json")
    val jsonString = file.readText()
    val departments_show = gson.fromJson(jsonString, Array<Departments>::class.java)

    // Вывод данных в консоль
    for (department in departments_show) {
        Log.d("MainActivity", "ID: ${department.id}, Имя: ${department.name}")
        // Добавьте другие свойства по необходимости
    }
}