package com.pixeldev.compose.presentation.home

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.pixeldev.compose.R
import com.pixeldev.compose.core.common.Utlis.CommonAlertDialog
import com.pixeldev.compose.presentation.create.QrGeneratorScreen
import com.pixeldev.compose.presentation.history.QrListScreen
import com.pixeldev.compose.presentation.saved.FavoriteScreen
import com.pixeldev.compose.presentation.QrViewModel
import com.pixeldev.compose.presentation.drawer.AboutScreen
import com.pixeldev.compose.presentation.drawer.ContactUsScreen
import com.pixeldev.compose.presentation.drawer.DummyListScreen
import com.pixeldev.compose.presentation.drawer.FAQsScreen
import com.pixeldev.compose.presentation.drawer.PrivacyPolicyScreen
import com.pixeldev.compose.presentation.drawer.TermsConditionsScreen
import com.pixeldev.compose.presentation.navigation.AppRoutes
import com.pixeldev.compose.presentation.scan.ScanQrScreen
import com.pixeldev.compose.ui.theme.DarkSurface
import com.pixeldev.compose.ui.theme.PrimaryAccent
import com.pixeldev.compose.ui.theme.PrimaryText
import com.pixeldev.compose.ui.theme.SecondaryAccent

@Composable
fun HomeScreen(navController: NavHostController) {
    RootNavigationHome()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(rootNavController: NavHostController) {
    val bottomItems = listOf(
        BottomNavItem.Create,
        BottomNavItem.History,
        BottomNavItem.Scan,
        BottomNavItem.Saved,
        BottomNavItem.Settings
    )

    val drawerItemColors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = PrimaryAccent,
        unselectedContainerColor = Color.Transparent,
        selectedIconColor = Color.White,
        unselectedIconColor = Color.White.copy(alpha = 0.7f),
        selectedTextColor = Color.White,
        unselectedTextColor = Color.White.copy(alpha = 0.7f)
    )

    val bottomNavController = rememberNavController()

    // 💡 We need the state of the bottom nav controller for the bottom bar and FAB
    val bottomCurrentBackStack by bottomNavController.currentBackStackEntryAsState()
    val bottomCurrentRoute =
        bottomCurrentBackStack?.destination?.route ?: BottomNavItem.History.route

    // 💡 We need the state of the ROOT nav controller for the drawer selection
    val rootCurrentBackStack by rootNavController.currentBackStackEntryAsState()
    val rootCurrentRoute = rootCurrentBackStack?.destination?.route

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    //  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Handle back press
    BackHandler(enabled = bottomCurrentRoute != BottomNavItem.History.route) {
        if (bottomNavController.previousBackStackEntry != null) {
            bottomNavController.popBackStack()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.background(Color(0xFF1F1F1F)),
                windowInsets = WindowInsets.systemBars.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.End
                )
            ) {
                val context = LocalContext.current as Activity
                val showExitDialog = remember { mutableStateOf(false) }
                // 💡 Apply background to a Box/Column inside the drawer sheet
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                        .background(DarkSurface)
                        .padding(top = 60.dp)
                        .padding(20.dp)
                ) {
                    // Profile Section
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = rememberAsyncImagePainter(R.drawable.ic_launcher), // Replace with actual URL
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop, // Ensure image fills the circle
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(3.dp, SecondaryAccent, CircleShape)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.app_name),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // --- Drawer Navigation Items ---

                    NavigationDrawerItem(
                        label = { Text("About Us", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Outlined.EventNote,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        // 💡 Use rootCurrentRoute for selection logic
                        selected = rootCurrentRoute == AppRoutes.ABOUT_US,
                        onClick = {
                            rootNavController.navigate(AppRoutes.ABOUT_US)
                            scope.launch { drawerState.close() }
                        },
                        colors = drawerItemColors
                    )

                    NavigationDrawerItem(
                        label = { Text("Privacy Policy", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Outlined.Description,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        // 💡 Use rootCurrentRoute for selection logic
                        selected = rootCurrentRoute == AppRoutes.PRIVACY_POLICY,
                        onClick = {
                            rootNavController.navigate(AppRoutes.PRIVACY_POLICY)
                            scope.launch { drawerState.close() }
                        },
                        colors = drawerItemColors
                    )

                    // Terms & Conditions
                    NavigationDrawerItem(
                        label = { Text("Terms & Conditions", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Outlined.Gavel,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        // 💡 Use rootCurrentRoute for selection logic
                        selected = rootCurrentRoute == AppRoutes.TERMS_CONDITIONS,
                        onClick = {
                            rootNavController.navigate(AppRoutes.TERMS_CONDITIONS)
                            scope.launch { drawerState.close() }
                        },
                        colors = drawerItemColors
                    )

                    // Contact Us
                    NavigationDrawerItem(
                        label = { Text("Contact Us", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Outlined.HeadsetMic,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        // 💡 Use rootCurrentRoute for selection logic
                        selected = rootCurrentRoute == AppRoutes.CONTACT_US,
                        onClick = {
                            rootNavController.navigate(AppRoutes.CONTACT_US)
                            scope.launch { drawerState.close() }
                        },
                        colors = drawerItemColors
                    )

                    // FAQs
                    NavigationDrawerItem(
                        label = { Text("FAQs", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Outlined.QuestionAnswer,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        // 💡 Use rootCurrentRoute for selection logic
                        selected = rootCurrentRoute == AppRoutes.FAQS,
                        onClick = {
                            rootNavController.navigate(AppRoutes.FAQS)
                            scope.launch { drawerState.close() }
                        },
                        colors = drawerItemColors
                    )

                  /*  // Delete Account
                    NavigationDrawerItem(
                        label = { Text("Delete Account", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        // 💡 Use rootCurrentRoute for selection logic
                        selected = rootCurrentRoute == AppRoutes.DELETE_ACCOUNT,
                        onClick = {
                            rootNavController.navigate(AppRoutes.DELETE_ACCOUNT)
                            scope.launch { drawerState.close() }
                        },
                        colors = drawerItemColors
                    )*/

                    Spacer(modifier = Modifier.weight(1f))

                    // Logout button at the bottom
                    NavigationDrawerItem(
                        label = { Text("Exit", color = PrimaryText) },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                tint = PrimaryText
                            )
                        },
                        selected = false,
                        onClick = {
                            showExitDialog.value = true
                        },
                        colors = drawerItemColors
                    )
                }
                // --- Exit App Dialog ---
                CommonAlertDialog(
                    showDialog = showExitDialog.value,
                    onDismissRequest = {
                        showExitDialog.value = false
                    }, // Dismiss if user taps outside
                    title = "Exit Application?",
                    message = "Are you sure you want to close the app?",
                    onConfirm = {
                        // This will close the current activity and exit the app
                        context.finish()
                        showExitDialog.value = false // Explicitly hide the dialog
                    },
                    onCancel = {
                        Toast.makeText(context, "Exit cancelled.", Toast.LENGTH_SHORT).show()
                        showExitDialog.value = false // Hide the dialog
                    },
                    confirmButtonText = "Yes, Exit",
                    cancelButtonText = "No, Stay"
                )

            }
        }
    ) {
        Scaffold(
            //modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DarkSurface,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    title = {
                        Text(
                            // We use bottomCurrentRoute here to get the title for the main screens
                            getPageTitle(bottomCurrentRoute.toString()),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Rounded.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: notifications */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alerts")
                        }
                    },
                    // scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                BottomNavigationBar(bottomNavController, bottomItems, bottomCurrentRoute)
            },
            floatingActionButton = {
                Box() {
                    FloatingActionButton(
                        onClick = {
                            bottomNavController.navigate(BottomNavItem.Scan.route)
                        },
                        containerColor = PrimaryAccent,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(70.dp)
                            .offset(y = 50.dp)
                            .border(
                                width = 3.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = BottomNavItem.Scan.icon,
                            contentDescription = "Scan",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { padding ->
            val viewModel: QrViewModel = hiltViewModel()
            BottomNavGraph(
                navController = bottomNavController,
                modifier = Modifier.padding(padding),
                viewModel
            )
        }
    }
}

// --- BottomNavGraph.kt ---
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: QrViewModel
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Create.route, // <- Change this
        modifier = modifier
    ) {
        composable(BottomNavItem.Create.route) { QrGeneratorScreen(viewModel) }
        composable(BottomNavItem.History.route) { QrListScreen(viewModel) }
        composable(BottomNavItem.Scan.route) {
            ScanQrScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(BottomNavItem.Saved.route) { FavoriteScreen(viewModel) }
        composable(BottomNavItem.Settings.route) { Screen("Settings Screen") }
    }

}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = DarkSurface,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
            if (item == BottomNavItem.Scan) {
                Box(
                    modifier = Modifier
                        .weight(1f, fill = true),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.offset(y = 20.dp) // move label down to match others
                    ) {
                        //Spacer(modifier = Modifier.height(36.dp)) // match icon height visually
                        Text(
                            text = "Scan",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentRoute == BottomNavItem.Scan.route) PrimaryAccent else Color.White.copy(
                                alpha = 0.7f
                            )
                        )
                    }
                }
            } else {
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                // saveState = true
                            }
                            launchSingleTop = true
                            //restoreState = true
                        }
                    },
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = item.label)
                    },
                    label = {
                        Text(item.label, fontWeight = FontWeight.Bold)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryAccent,
                        selectedTextColor = PrimaryAccent,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color.White.copy(alpha = 0.7f),
                        unselectedTextColor = Color.White.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}

@Composable
fun getPageTitle(route: String) = when (route) {
    BottomNavItem.History.route -> "History"
    BottomNavItem.Create.route -> "Create"
    BottomNavItem.Scan.route -> "Scan"
    BottomNavItem.Saved.route -> "Saved"
    BottomNavItem.Settings.route -> "Settings"
    else -> "App"
}

@Composable
fun Screen(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
    ) {
        Text(text = text, color = Color.White)
        DummyListScreen()
    }
}

// --- AppRoutes.kt (Updated) ---


// --- RootNavigation.kt (Updated) ---

@Composable
fun RootNavigationHome() {
    val context = LocalContext.current
    val rootNavController = rememberNavController()
    val showDeleteAccountDialog = remember { mutableStateOf(false) }

    NavHost(navController = rootNavController, startDestination = AppRoutes.MAIN_SCREEN) {
        composable(AppRoutes.MAIN_SCREEN) {
            MainScreen(rootNavController = rootNavController)
        }

        // Define full-screen Composables for drawer navigation
        composable(AppRoutes.ABOUT_US) {
            AboutScreen(navController = rootNavController)
        }
        composable(AppRoutes.PRIVACY_POLICY) {
            PrivacyPolicyScreen(navController = rootNavController)
        }

        // New: Terms & Conditions Screen
        composable(AppRoutes.TERMS_CONDITIONS) {
            TermsConditionsScreen(navController = rootNavController)
        }

        // New: Contact Us Screen
        composable(AppRoutes.CONTACT_US) {
            ContactUsScreen(navController = rootNavController)
        }

        // New: FAQs Screen
        composable(AppRoutes.FAQS) {
            FAQsScreen(navController = rootNavController)
        }

        // New: Delete Account Screen
        composable(AppRoutes.DELETE_ACCOUNT) {
           // showDeleteAccountDialog.value = true
        }
    }

    // --- Delete Account Dialog ---
    CommonAlertDialog(
        showDialog = showDeleteAccountDialog.value,
        onDismissRequest = {
            showDeleteAccountDialog.value = false
        }, // Dismiss if user taps outside
        title = "Delete Account",
        subtitle = "Permanent Action",
        message = "WARNING: Deleting your account is a permanent action and all your data will be lost. This cannot be undone.",
        onConfirm = {
            // --- Your Account Deletion Logic Here ---
            Toast.makeText(context, "Initiating account deletion...", Toast.LENGTH_LONG).show()
            Log.d("AppDialogsDemo", "User confirmed account deletion.")
            // You would typically call a ViewModel function here to perform the actual deletion:
            // viewModel.deleteAccount()
            // And then navigate away or show a success message

            showDeleteAccountDialog.value = false // Hide the dialog after action
        },
        onCancel = {
            Toast.makeText(context, "Account deletion cancelled.", Toast.LENGTH_SHORT).show()
            showDeleteAccountDialog.value = false // Hide the dialog
        },
        confirmButtonText = "Delete My Account",
        cancelButtonText = "Keep Account"
    )
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object History : BottomNavItem("history", Icons.Default.History, "History")
    object Create : BottomNavItem("create", Icons.Default.Add, "Create")
    object Scan : BottomNavItem("scan", Icons.Default.QrCodeScanner, "") // placeholder
    object Saved : BottomNavItem("saved", Icons.Default.Favorite, "Saved")
    object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")

}

