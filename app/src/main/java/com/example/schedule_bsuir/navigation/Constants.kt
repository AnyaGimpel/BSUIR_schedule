package com.example.schedule_bsuir.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Поиск",
            icon = Icons.Filled.Search,
            route = "find"
        ),
        BottomNavItem(
            label = "Просмотр",
            icon = Icons.Filled.DateRange,
            route = "look"
        )
    )
}