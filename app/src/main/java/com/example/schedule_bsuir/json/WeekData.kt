package com.example.schedule_bsuir.json

import android.content.Context
import com.example.schedule_bsuir.R
import com.google.gson.Gson
import java.io.InputStreamReader

data class WeekData(val weekNumber: Int, val weeks: List<String>)

fun getWeekNumber(context: Context, date: String): Int? {
    try {
        // Открываем файл из res/raw
        val inputStream = context.resources.openRawResource(R.raw.dates)

        // Читаем содержимое файла в список объектов WeekData с помощью Gson
        val reader = InputStreamReader(inputStream)
        val weekDataList = Gson().fromJson(reader, Array<WeekData>::class.java)

        // Ищем номер недели по заданной дате в каждом объекте WeekData
        for (weekData in weekDataList) {
            if (weekData.weeks.contains(date)) {
                return weekData.weekNumber
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}