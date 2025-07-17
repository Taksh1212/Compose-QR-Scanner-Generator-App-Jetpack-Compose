package com.pixeldev.compose.presentation.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pixeldev.compose.presentation.navigation.AppNavHost
import com.pixeldev.compose.ui.theme.ComposeKtorDemoTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBarDefaults.windowInsets
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
class MainAppScreen  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Set RootNavigation as the entry point
               // RootNavigation()
            }
        }
    }
}
// Ap
/*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(rootNavController: NavHostController) { // <-- 1. Accept the root controller
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home),
        BottomNavItem(Screen.Search.route, "Search", Icons.Default.Search),
        BottomNavItem(Screen.Reels.route, "Reels", Icons.Default.Slideshow),
        BottomNavItem(Screen.Shop.route, "Shop", Icons.Default.ShoppingBag),
        BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.AccountCircle)
    )

    val drawerNavItems = listOf(
        DrawerItem(Screen.Settings.route, "Settings", Icons.Default.Settings),
        DrawerItem(Screen.Archive.route, "Archive", Icons.Default.Archive),
        DrawerItem(Screen.Activity.route, "Your Activity", Icons.Default.History),
        DrawerItem(Screen.QrCode.route, "QR Code", Icons.Default.QrCodeScanner),
        DrawerItem(Screen.Saved.route, "Saved", Icons.Default.BookmarkBorder),
        DrawerItem(Screen.CloseFriends.route, "Close Friends", Icons.Default.List)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                drawerNavItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            // 2. Use the ROOT controller to navigate
                            rootNavController.navigate(item.route)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = currentRoute?.toTitleCase() ?: "Home") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                // 3. Use the BOTTOM controller for bottom nav
                                bottomNavController.navigate(item.route) {
                                    popUpTo(bottomNavController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = bottomNavController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

// Helper data classes
data class BottomNavItem(val route: String, val title: String, val icon: ImageVector)
data class DrawerItem(val route: String, val title: String, val icon: ImageVector)

// Helper extension function
fun String.toTitleCase(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        .replace("_", " ")
}
// Navigation.kt

sealed class Screen(val route: String, val title: String) {
    // Bottom Nav Screens
    object Home : Screen("home", "Home")
    object Search : Screen("search", "Search")
    object Reels : Screen("reels", "Reels")
    object Shop : Screen("shop", "Shop")
    object Profile : Screen("profile", "Profile")

    // Drawer Screens
    object Settings : Screen("settings", "Settings")
    object Archive : Screen("archive", "Archive")
    object Activity : Screen("activity", "Your Activity")
    object QrCode : Screen("qrcode", "QR Code")
    object Saved : Screen("saved", "Saved")
    object CloseFriends : Screen("close_friends", "Close Friends")
}
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // This graph only contains pages for the bottom navigation
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // Bottom Navigation Screens
        composable(Screen.Home.route) { HomePage1("Home") }
        composable(Screen.Search.route) { HomePage1("Search") }
        composable(Screen.Reels.route) { HomePage1("Reels") }
        composable(Screen.Shop.route) { HomePage1("SHop") }
        composable(Screen.Profile.route) { HomePage1("Profile") }
    }
}

const val MAIN_APP_ROUTE = "main_app"

@Composable
fun RootNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MAIN_APP_ROUTE) {
        // 1. This is the destination for your entire Scaffold with the Bottom Nav
        composable(MAIN_APP_ROUTE) {
            // Pass the root nav controller to the scaffold
            AppScaffold(rootNavController = navController)
        }

        // 2. Define routes for each drawer page at the top level
        // These will be full-screen pages

        // Drawer Navigation Screens
        composable(Screen.Settings.route) { SettingsPage("Settings") }
        composable(Screen.Archive.route) { SettingsPage("Archive") }
        composable(Screen.Activity.route) { SettingsPage("Activity") }
        composable(Screen.QrCode.route) { SettingsPage("QrCode") }
        composable(Screen.Saved.route) { SettingsPage("Saved") }
        composable(Screen.CloseFriends.route) { SettingsPage("CloseFriends") }
    }
}
@Composable
fun HomePage1(str: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("$str Page", fontSize = 24.sp)
    }
}@Composable
fun SettingsPage(str: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("$str Page", fontSize = 24.sp)
    }
}
*/
