package com.example.latihanmodul5

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail")
}
