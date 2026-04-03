package com.example.tugas5

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import kotlinx.coroutines.launch

private val GreenPrimary = Color(0xFF2E7D32)
private val GreenSecondary = Color(0xFF4CAF50)
private val WhiteBackground = Color(0xFFFFFFFF)
private val LightGreenContainer = Color(0xFFE8F5E9)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = LightGreenContainer,
    onPrimaryContainer = GreenPrimary,
    secondary = GreenSecondary,
    onSecondary = Color.White,
    background = WhiteBackground,
    surface = WhiteBackground,
    onSurface = Color.Black,
    surfaceVariant = LightGreenContainer
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val repository = remember { NoteRepository() }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    MaterialTheme(colorScheme = LightColorScheme) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val showNavigationElements = currentDestination?.route in listOf(
            BottomBarScreen.Notes.route,
            BottomBarScreen.Favorites.route,
            BottomBarScreen.Profile.route
        )

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = showNavigationElements,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Notes App",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        color = GreenPrimary
                    )
                    @Suppress("DEPRECATION")
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Notes") },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Notes.route } == true,
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.Notes.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Favorites") },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Favorites.route } == true,
                        icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.Favorites.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Profile") },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Profile.route } == true,
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.Profile.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "v1.0.0",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        ) {
            Scaffold(
                bottomBar = {
                    if (showNavigationElements) {
                        NavigationBar(
                            containerColor = WhiteBackground,
                            contentColor = GreenPrimary
                        ) {
                            val items = listOf(
                                BottomBarScreen.Notes,
                                BottomBarScreen.Favorites,
                                BottomBarScreen.Profile
                            )
                            items.forEach { screen ->
                                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = { Text(screen.title) },
                                    selected = selected,
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenPrimary,
                                        selectedTextColor = GreenPrimary,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray,
                                        indicatorColor = LightGreenContainer
                                    ),
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            val startDest = navController.graph.findStartDestination()
                                            popUpTo(startDest.route ?: return@navigate) {
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
                            },
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                    }
                    composable(Screen.Favorites.route) {
                        FavoritesScreen(
                            repository = repository,
                            onNoteClick = { noteId ->
                                navController.navigate(Screen.NoteDetail.createRoute(noteId))
                            },
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                    }
                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
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
}
