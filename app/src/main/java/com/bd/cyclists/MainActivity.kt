package com.bd.cyclists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bd.cyclists.ui.HomeScreen
import com.bd.cyclists.ui.ProfileScreen
import com.bd.cyclists.ui.RecordScreen
import com.bd.cyclists.ui.SearchScreen
import com.bd.cyclists.ui.theme.BdCyclistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BdCyclistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BdCyclistScreenStructure()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BdCyclistScreenStructure() {
    val navController = rememberNavController() // Get NavController
    Scaffold(
        topBar = {
            TopBarContent(navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, startDestination = "record",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("maps") { /* Content for Maps screen */ Text("Maps Screen Content") }
            composable("record") { RecordScreen() }
            composable("groups") { /* Content for Groups screen */ Text("Groups Screen Content") }
            composable("you") { /* Content for You screen */ Text("You Screen Content") }

            composable(
                "profile",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }) { ProfileScreen() }

            composable("search") { SearchScreen() }
            composable(
                "settings", enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }) { /* Content for Settings screen */ Text("Settings Screen Content") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStravaTopAppBarWithBack() {
    MaterialTheme {
        TopBarContent(rememberNavController()) // Provide a dummy NavController for preview
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBackButton =
        currentRoute == "profile" || currentRoute == "settings" || currentRoute == "search" // Routes where back button should appear

    TopAppBar(
        title = {
            if (showBackButton) {
                Text(currentRoute.uppercase())
            } else {
                Text(
                    stringResource(R.string.app_name)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors( // Apply colors here
            containerColor = MaterialTheme.colorScheme.primary, // Use theme's primary color
            titleContentColor = Color.White, // Set title color to white
            actionIconContentColor = Color.White, // Set action icon color to white,
            navigationIconContentColor = Color.White // Set color for navigation icon
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Use AutoMirrored for LTR/RTL support
                        contentDescription = "Back"
                    )
                }
            } else {
                // If you want to show something on the left when not showing back button, e.g., a logo
                // Spacer(Modifier.width(0.dp)) // Or leave empty if nothing is needed
            }
        },
        actions = {
            Row {
                // User Image
                Image(
                    imageVector = Icons.Default.Person, // Replace with your user image drawable/painter
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate("profile") } // Added click action here
                        .padding(8.dp) // Add padding to make the circle visible
                )

                // Search Icon
                IconButton(onClick = { navController.navigate("search") }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                    )
                }

                // Settings Icon
                IconButton(onClick = { navController.navigate("settings") }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }
    )
}

@Composable
fun BottomBarContent() {
    BottomAppBar(
        modifier = Modifier.padding(top = 8.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer // Example: using secondaryContainer, you can choose primary, surface, etc.
    ) {
        Text("Bottom Bar Content", modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBdCyclistScreenStructure() {
    BdCyclistTheme {
        BdCyclistScreenStructure()
    }
}


// Data class to represent each item in the bottom navigation
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String // Not strictly needed for just design, but good practice
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Maps", Icons.Default.LocationOn, "maps"),
        BottomNavItem("Record", Icons.Default.PlayArrow, "record"), // Changed icon for Record
        BottomNavItem("Groups", Icons.Default.Person, "groups"),
        BottomNavItem("You", Icons.Default.Menu, "you")
    )

    // Remember the currently selected item
    var selectedItem by remember { mutableStateOf(items[0]) } // Home is initially selected

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface // Use surface for bottom bar as in the screenshot
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == item.route, // Update selection logic
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors( // Apply colors here
                    selectedIconColor = MaterialTheme.colorScheme.primary, // Color when selected
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant // Color when unselected
                    // You can also set selectedTextColor and unselectedTextColor here
                )
            )
        }
    }
}