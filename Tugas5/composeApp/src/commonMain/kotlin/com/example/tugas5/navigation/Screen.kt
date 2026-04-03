package com.example.tugas5.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object AddNote : Screen("add_note")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: String) = "note_detail/$noteId"
    }
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: String) = "edit_note/$noteId"
    }
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Notes : BottomBarScreen(
        route = Screen.Notes.route,
        title = "Notes",
        icon = Icons.Default.Home
    )
    object Favorites : BottomBarScreen(
        route = Screen.Favorites.route,
        title = "Favorites",
        icon = Icons.Default.Favorite
    )
    object Profile : BottomBarScreen(
        route = Screen.Profile.route,
        title = "Profile",
        icon = Icons.Default.Person
    )
}
