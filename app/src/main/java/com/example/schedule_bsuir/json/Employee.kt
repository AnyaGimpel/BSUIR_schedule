package com.example.schedule_bsuir.json

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File

interface MyAPI {
    @GET("/api/v1/employees/all")
    fun getEmployees(): Call<Array<Employee>>
}

data class Employee(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val degree: String,
    val rank: String,
    //val photoLink: String,
    //val calendarId: String,
    val academicDepartment: List<String>,
    val id: Int,
    val urlId: String,
    val fio: String
)

fun getEmployees(context: Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://iis.bsuir.by")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(MyAPI::class.java)

    api.getEmployees().enqueue(object : Callback<Array<Employee>> {
        override fun onResponse(call: Call<Array<Employee>>, response: Response<Array<Employee>>) {
            if (response.isSuccessful) {
                val employees = response.body()

                val gson = GsonBuilder()
                    .setPrettyPrinting()
                    .create()

                val json = gson.toJson(employees)
                val file = File(context.getExternalFilesDir(null), "employees.json")
                try {
                    file.writeText(json)
                    Log.d("MainActivity", "Данные успешно записаны в файл employees.json")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Ошибка при записи данных в файл: $e")
                }
            } else {
                Log.e("API connection", "Ошибка подключения API")
            }

        }

        override fun onFailure(call: Call<Array<Employee>>, t: Throwable) {
            Log.e("API connection", "Ошибка подключения API")
        }
    })
}

fun readEmployees(context: Context) {
    val gson = Gson()
    val file = File(context.getExternalFilesDir(null), "employees.json")
    val jsonString = file.readText()
    val employees_show = gson.fromJson(jsonString, Array<Employee>::class.java)

    // Вывод данных в консоль
    for (employee in employees_show) {
        Log.d("MainActivity", "Имя: ${employee.firstName}, Фамилия: ${employee.lastName}")
    }
}