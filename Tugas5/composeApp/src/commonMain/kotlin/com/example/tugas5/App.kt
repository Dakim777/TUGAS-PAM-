package com.example.tugas5

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tugas5.data.NoteRepository
import com.example.tugas5.navigation.BottomBarScreen
import com.example.tugas5.navigation.Screen
import com.example.tugas5.screens.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val repository = remember { NoteRepository() }

    MaterialTheme {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val showBottomBar = currentDestination?.route in listOf(
            BottomBarScreen.Notes.route,
            BottomBarScreen.Favorites.route,
            BottomBarScreen.Profile.route
        )

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        val items = listOf(
                            BottomBarScreen.Notes,
                            BottomBarScreen.Favorites,
                            BottomBarScreen.Profile
                        )
                        items.forEach { screen ->
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = screen.title) },
                                label = { Text(screen.title) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Notes.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Notes.route) {
                    NotesScreen(
                        repository = repository,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onAddNoteClick = {
                            navController.navigate(Screen.AddNote.route)
                        }
                    )
                }
                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        repository = repository,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileScreen()
                }
                composable(Screen.AddNote.route) {
                    AddNoteScreen(
                        repository = repository,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                    NoteDetailScreen(
                        noteId = noteId,
                        repository = repository,
                        onBack = { navController.popBackStack() },
                        onEditClick = { id ->
                            navController.navigate(Screen.EditNote.createRoute(id))
                        }
                    )
                }
                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                    EditNoteScreen(
                        noteId = noteId,
                        repository = repository,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
